package edu.tamu.cap.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.DC;
import org.fcrepo.client.DeleteBuilder;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.fcrepo.client.GetBuilder;
import org.fcrepo.client.PatchBuilder;
import org.fcrepo.client.PostBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.cap.exceptions.IRVerificationException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.ircontext.FedoraTransactionDetails;
import edu.tamu.cap.model.ircontext.TransactionDetails;
import edu.tamu.cap.model.response.FixityReport;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;
import edu.tamu.cap.util.StringUtil;

@Service("Fedora")
public class FedoraService implements IRService<Model>, VersioningIRService<Model>, VerifyingIRService<Model>, TransactingIRService<Model>, QueryableIRService<Model>, FixityIRService<Model> {
    
    private final static String LDP_CONTAINS_PREDICATE = "http://www.w3.org/ns/ldp#contains";

    private final static String RDF_TYPE_PREDICATE = "https://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    private final static String EBU_FILENAME_PREDICATE = "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#filename";

    private final static String FEDORA_ROOT_PREDICATE = "http://fedora.info/definitions/v4/repository#RepositoryRoot";

    private final static String FEDORA_HAS_PARENT_PREDICATE = "http://fedora.info/definitions/v4/repository#hasParent";

    private final static String FEDORA_CONTAINER_PREDICATE = "http://fedora.info/definitions/v4/repository#Container";

    public final static String FEDORA_BINRAY_PREDICATE = "http://fedora.info/definitions/v4/repository#Binary";

    public final static String FEDORA_HAS_VERSIONS_PREDICATE = "http://fedora.info/definitions/v4/repository#hasVersions";

    public final static String FEDORA_HAS_VERSION_PREDICATE = "http://fedora.info/definitions/v4/repository#hasVersion";

    public final static String FEDORA_HAS_VERSION_LABEL = "http://fedora.info/definitions/v4/repository#hasVersionLabel";

    public final static String FEDORA_CREATED = "http://fedora.info/definitions/v4/repository#created";

    public final static String FEDORA_VERSION = "http://fedora.info/definitions/v4/repository#Version";

    // @formatter:off
    private final static String[] PROPERTY_PREFIXES = new String[] {
        "http://fedora.info/definitions/v4/repository",
        "http://www.w3.org/1999/02/22-rdf-syntax-ns",
        "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore",
        "http://www.iana.org/assignments/relation",
        "http://www.loc.gov/premis/rdf/v1",
        "http://pcdm.org/models"
    };
    // @formatter:on
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private IR ir;

    @Override
    public void verifyPing() throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
        if (response.getStatusCode() == -1) {
            throw new IRVerificationException("No response from " + ir.getRootUri());
        }
    }

    @Override
    public void verifyAuth() throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
        if (response.getStatusCode() != 200) {
            throw new IRVerificationException("Authenitcation failed. Status " + response.getStatusCode());
        }
    }

    @Override
    public void verifyRoot() throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri() + "/fcr:metadata"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModel(response.getBody());
        Optional<String> root = getLiteralForProperty(model, model.createProperty(FEDORA_ROOT_PREDICATE));
        if (root.isPresent()) {
            throw new IRVerificationException("URI is not a Fedora root!");
        }
    }
    
    @Override
    public IRContext getIRContext(String contextUri) throws Exception {
        
        Optional<Cookie> cookie = Optional.empty();
        
        Cookie[] cookies = request.getCookies(); 
        
        if(cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("transaction")) {
                    cookie = Optional.of(c);
                    break;
                }
            }
        }
        
        if (cookie.isPresent() && !contextUri.contains("tx:")) {
            JsonNode cookieObject = objectMapper.readTree(URLDecoder.decode(cookie.get().getValue(), "UTF-8"));
            
            String transactionToken = cookieObject.get("transactionToken").asText();
            
            String contextPath = contextUri.replace(ir.getRootUri(), "");
                    
            URI transactionURI = URI.create(transactionToken);
            URI rootURI = URI.create(ir.getRootUri());
            
            StringBuilder strngBldr = new StringBuilder();
            
            strngBldr
                .append(transactionToken.contains("https://")?"https":"http")
                .append("://")
                .append(rootURI.getHost());
            
            if(!rootURI.getHost().endsWith("/") && !transactionURI.getPath().startsWith("/")) strngBldr.append("/");
            
            if(rootURI.getHost().endsWith("/") && transactionURI.getPath().startsWith("/")) {
                strngBldr.append(transactionURI.getPath().substring(1));
            } else {
                strngBldr.append(transactionURI.getPath());
            }
            
            if(!transactionURI.getPath().endsWith("/")&&!contextPath.startsWith("/")) strngBldr.append("/").append(contextPath);
            
            if(transactionURI.getPath().endsWith("/")&&contextPath.startsWith("/")) strngBldr.append("/").append(contextPath.substring(1));
            
            contextUri = strngBldr.toString();
            
        }
        
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(contextUri + "/fcr:metadata"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModel(response.getBody());
        return buildIRContext(model, contextUri);
    }
    
    @Override
    public void deleteIRContext(String uri) throws Exception {
        logger.info("Deletion or contianer: {}", uri);
        deleteContainer(uri);
    }
    
    @Override
    public List<Triple> getTriples(IRService<?> irService, String contextUri) throws Exception {
        IRContext context = getIRContext(contextUri);
        
        List<Triple> allTriples = new ArrayList<Triple>();
        
        allTriples.addAll(context.getProperties());
        allTriples.addAll(context.getMetadata());
        //TODO add all resource triples
        
        return allTriples;
    }

    @Override
    public IRContext createChild(String contextUri, List<Triple> metadata) throws Exception {
        FcrepoClient client = buildClient();
        PostBuilder post = new PostBuilder(new URI(contextUri), client);
        
        Model model = ModelFactory.createDefaultModel();
        metadata.forEach(metadatum->{
          Property prop = model.createProperty(metadatum.getPredicate());
          model.createResource("").addProperty(prop, metadatum.getObject());
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          RDFDataMgr.write(out, model, Lang.TURTLE);
          post.body(new ByteArrayInputStream(out.toByteArray()), "text/turtle");
        });
        
        FcrepoResponse response = post.perform();
        URI location = response.getLocation();
        logger.debug("Container creation status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }
    
    @Override
    public List<Triple> getChildren(String contextUri) throws Exception {
        List<Triple> childrenTriples = new ArrayList<Triple>();
        getIRContext(contextUri).getChildren().forEach(childContext->{
            childrenTriples.add(childContext.getTriple());
        });
        return childrenTriples;
    }
    
    @Override
    public IRContext createMetadata(String contextUri,Triple triple) throws Exception {
        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);
        String sparql = "INSERT { " + triple.toString() + " . } WHERE {}";

        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata creation status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }
    

    @Override
    public List<Triple> getMetadata(String contextUri) throws Exception {
        IRContext context = getIRContext(contextUri);
        return context.getMetadata();
    }

    @Override
    public IRContext updateMetadata(String contextUri, Triple originalTriple, String newValue) throws Exception {
                       
        StringBuilder stngBldr = new StringBuilder();
        stngBldr.append("DELETE { <> <").append(originalTriple.getPredicate()).append("> '").append(StringUtil.removeQuotes(originalTriple.getObject())).append("' } ");
        stngBldr.append("INSERT { <> <").append(originalTriple.getPredicate()).append("> '").append(StringUtil.removeQuotes(newValue)).append("' } ");
        stngBldr.append("WHERE { }");
        String sparql = stngBldr.toString();
               
        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);
        patch.body(new ByteArrayInputStream(sparql.getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata update status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }
    
    @Override
    public IRContext deleteMetadata(String contextUri, Triple triple) throws Exception {

        logger.debug("Attempting to delete");

        FcrepoClient client = buildClient();

        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);

        String sparql = "DELETE WHERE { " + triple.toString() + " } ";

        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata delete status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }

    @Override
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception {
        FcrepoClient client = buildClient();
        PostBuilder post = new PostBuilder(new URI(contextUri), client);
        post.body(file.getInputStream(), file.getContentType());
        post.filename(file.getOriginalFilename());
        FcrepoResponse response = post.perform();
        URI location = response.getLocation();
        logger.debug("Resource creation status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }
    
    @Override
    public IRContext getResource(String contextUri) throws Exception {
        return getIRContext(contextUri);
    }

    @Override
    public void deleteResource(String contextUri) throws Exception {
        logger.info("Deleting resource: {}", contextUri);
        deleteContainer(contextUri);
    }

    @Override
    public IRContext resourceFixity(String contextUri) throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(URI.create(contextUri + "/fcr:fixity"), client).perform();

        Model model = ModelFactory.createDefaultModel();
        model.read(response.getBody(), null, "text/turtle");
        model.createResource("").addProperty(DC.title, "Fixity Report");

        FixityReport fixityReportContext = FixityReport.of(buildIRContext(model, contextUri).getProperties());
        IRContext thisContext = getIRContext(contextUri);
        thisContext.setFixity(fixityReportContext);

        return thisContext;
    }

    @Override
    public List<Version> getVersions(String contextUri) throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(contextUri + "/fcr:versions"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModel(response.getBody());
        List<Version> versions = new ArrayList<Version>();

        model.listObjectsOfProperty(model.createProperty(FEDORA_HAS_VERSION_PREDICATE)).forEachRemaining(object -> {

            Version version = Version.of(Triple.of(contextUri + "/fcr:versions", FEDORA_HAS_VERSION_PREDICATE, object.toString()));

            object.asResource().listProperties().forEachRemaining(prop -> {
                switch (prop.getPredicate().toString()) {
                case FEDORA_HAS_VERSION_LABEL:
                    version.setName(prop.getObject().toString());
                    break;
                case FEDORA_CREATED:
                    version.setTime(prop.getObject().toString());
                    break;
                }
            });

            versions.add(version);

        });

        return versions;
    }

    @Override
    public IRContext createVersion(String contextUri, String name) throws Exception {

        if (name.isEmpty()) {
            SimpleDateFormat output = new SimpleDateFormat("yyyyMMddHHmmss");
            name = "version." + output.format(System.currentTimeMillis());
        } else {
            name = "version." + name;
        }

        URI uri = URI.create(contextUri + "/fcr:versions");
        logger.info("Attempting to create version: {}", uri.toString());
        FcrepoResponse response = new PostBuilder(uri, buildClient()).slug(name).perform();

        URI location = response.getLocation();
        logger.info("Version creation status and location: {}, {}", response.getStatusCode(), location);

        return getIRContext(contextUri);
    }

    @Override
    public IRContext restoreVersion(String contextUri) throws Exception {
        IRContext versionContext = getIRContext(contextUri);
        String parentUri = versionContext.getParent().getSubject();
        URI uri = URI.create(contextUri);
        new PatchBuilder(uri, buildClient()).perform();
        return getIRContext(parentUri);
    }

    @Override
    public void deleteVersion(String uri) throws Exception {
        logger.info("Deleting version: {}", uri);
        deleteContainer(uri);
    }
    
    private FcrepoResponse deleteContainer(String uri) throws URISyntaxException, FcrepoOperationFailedException {
        FcrepoClient client = buildClient();
        URI newURI = new URI(uri);
        DeleteBuilder builder = new DeleteBuilder(newURI, client);
        
        return builder.perform();
    }
    
    @Override
    public TransactionDetails startTransaction() throws Exception {
               
        FcrepoClient client = buildClient();
    
        URI transactionContextURI = new URI(ir.getRootUri()+"fcr:tx");
        
        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();
        
        String fedoraDate = response.getHeaderValue("Expires");
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss z");
        ZonedDateTime expirationDate = ZonedDateTime.parse(fedoraDate,f);
        
        logger.debug("Transaction Start: {}",transactionContextURI);

        return makeTransactionDetails(response.getLocation().toString(), DateTimeFormatter.ISO_ZONED_DATE_TIME.format(expirationDate));
    }
    
    @Override
    public TransactionDetails refreshTransaction(String tokenURI) throws Exception {
               
        FcrepoClient client = buildClient();
    
        URI transactionContextURI = new URI(tokenURI+"/fcr:tx");
        
        
        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();
        
        String fedoraDate = response.getHeaderValue("Expires");
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss z");
        ZonedDateTime expirationDate = ZonedDateTime.parse(fedoraDate,f);
        
        String location = response.getLocation() != null ? response.getLocation().toString() : tokenURI;
        
        logger.debug("Transaction Refresh: {}",transactionContextURI);

        return makeTransactionDetails(location, DateTimeFormatter.ISO_ZONED_DATE_TIME.format(expirationDate));
    }
    
    @Override
    public void commitTransaction(String tokenURI) throws Exception {
               
        FcrepoClient client = buildClient();
    
        URI transactionContextURI = new URI(tokenURI+"/fcr:commit" );
        
        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();
        
        logger.debug("Transaction Commited: {}",response.getStatusCode());
    }
    
    @Override
    public void rollbackTransaction(String tokenURI) throws Exception {
               
        FcrepoClient client = buildClient();
    
        URI transactionContextURI = new URI(tokenURI+"/fcr:tx/fcr:rollback" );
        
        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();

        System.out.println("Transaction RollBack: "+tokenURI);
        System.out.println("Transaction RollBack: "+response.getStatusCode());
        
        logger.debug("Transaction RollBack: {}",response.getStatusCode());
    }
    
    @Override
    public TransactionDetails makeTransactionDetails(String transactionToken, String expirationString) throws Exception {
        FedoraTransactionDetails transactionDetails = new FedoraTransactionDetails(transactionToken, expirationString);
        return transactionDetails;
    }
    
    @Override
    public void setIr(IR ir) {
        this.ir = ir;
    }
    
    @Override
    public IRContext query(String contextUri, String sparql) throws Exception {
        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);
        patch.body(new ByteArrayInputStream(sparql.getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Advanced update query status and location: {}, {}", response.getStatusCode(), location);
        return getIRContext(contextUri);
    }

    @Override
    public String getQueryHelp() throws Exception {
        
        StringBuilder strbldr = new StringBuilder();
        
        ir.getSchemas().forEach(schema->{
            strbldr.append("PREFIX ").append(schema.getAbbreviation()).append(": <").append(schema.getNamespace()).append(">\n");
        });
        
        strbldr.append("\n\n");
        strbldr.append("DELETE { }\n");
        strbldr.append("INSERT { }\n");
        strbldr.append("WHERE { }\n\n");
        
        return strbldr.toString();
    }

    @Override
    public synchronized IRContext buildIRContext(Model model, String contextUri) throws JsonProcessingException, UnsupportedEncodingException, IOException {
        
        IRContext irContext = new IRContext(Triple.of(contextUri, RDF_TYPE_PREDICATE, FEDORA_CONTAINER_PREDICATE));

        // System.out.println("\n::\n");
        model.listStatements().forEachRemaining(statement -> {

            Triple triple = craftTriple(statement);

            // System.out.println();
            // System.out.println(" SUBJECT: " + triple.getSubject());
            // System.out.println(" PREDICATE: " + triple.getPredicate());
            // System.out.println(" OBJECT: " + triple.getObject());
            // System.out.println();

            {
                String predicate = triple.getPredicate();

                switch (predicate) {
                case FEDORA_HAS_PARENT_PREDICATE:
                    irContext.setParent(triple);
                    break;
                case LDP_CONTAINS_PREDICATE:
                    irContext.addChild(new IRContext(triple));
                    break;
                case FEDORA_HAS_VERSIONS_PREDICATE:
                    try {
                        irContext.setVersions(getVersions(triple.getSubject()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:

                    for (String prefix : PROPERTY_PREFIXES) {
                        if (predicate.startsWith(prefix)) {
                            irContext.addProperty(triple);
                        }
                    }

                    for (String prefix : ir.getMetadataPrefixes()) {
                        if (predicate.startsWith(prefix)) {
                            irContext.addMetadum(triple);
                        }
                    }
                }
            }

            {
                String object = triple.getObject();

                switch (object) {
                case FEDORA_BINRAY_PREDICATE:
                    irContext.getTriple().setObject(FEDORA_BINRAY_PREDICATE);
                    break;
                case FEDORA_VERSION:

                    String verionName = contextUri.substring(contextUri.lastIndexOf("/") + 1, contextUri.length());
                    irContext.setVersion(verionName);

                    irContext.setParent(Triple.of(object, FEDORA_HAS_PARENT_PREDICATE, contextUri.replace("/fcr:versions/" + verionName, "")));

                    break;
                default:

                }
            }

        });
        
        
        String rootFromContext = contextUri;
        if(rootFromContext.contains("tx:")) {
            rootFromContext = rootFromContext.replaceAll("tx:.*\\/", "");
        }
        
        if(rootFromContext.equals(ir.getRootUri())) {    
            irContext.setName("Root");
        } else {
            irContext.setName(contextUri);
        }

        if (irContext.isResource()) {
            Optional<String> fileName = getLiteralForProperty(model, model.createProperty(EBU_FILENAME_PREDICATE));
            if (fileName.isPresent()) {
                irContext.setName(fileName.get());
            }
        } else {
            Optional<String> title = getLiteralForProperty(model, DC.title);
            if (title.isPresent()) {
                irContext.setName(title.get());
            }
        }

        if (irContext.getIsVersion()) {
            irContext.setName(irContext.getName() + " (" + irContext.getVersion() + ")");
        }

        return featureSupport(irContext);
    }

    private FcrepoClient buildClient() {
        return (ir.getUsername() == null || ir.getPassword() == null) ? FcrepoClient.client().build() : FcrepoClient.client().credentials(ir.getUsername(), ir.getPassword()).build();
    }

    private Triple craftTriple(Statement statement) {
        String subject = statement.asTriple().getSubject().toString();
        String predicate = statement.asTriple().getPredicate().toString();
        String object = statement.asTriple().getObject().toString();
        return Triple.of(subject, predicate, object);
    }

    private Optional<String> getLiteralForProperty(Model model, Property property) {
        Optional<String> literal = Optional.empty();
        NodeIterator nodeIterator = model.listObjectsOfProperty(property);
        while (nodeIterator.hasNext()) {
            RDFNode node = nodeIterator.next();
            if (node.isLiteral()) {
                literal = Optional.of(node.toString());
                break;
            }
        }
        return literal;
    }

    private Model createRdfModel(InputStream stream) {
        Model model = ModelFactory.createDefaultModel();
        model.read(stream, null, "RDF/XML");

        // System.out.println("\n");
        // model.write(System.out, "JSON-LD");
        // model.write(System.out, "RDF/XML");
        // System.out.println("\n");

        return model;
    }

    public IR getIR() {
        return ir;
    }

}
