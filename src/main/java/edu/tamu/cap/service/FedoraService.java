package edu.tamu.cap.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.exceptions.IRVerificationException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.FixityReport;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;

@Service("Fedora")
public class FedoraService implements IRService<Model> {

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
    public IRContext createMetadata(Triple triple) throws Exception {
        FcrepoClient client = buildClient();
        String contextUri = triple.getSubject();
        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);
        String sparql = "INSERT { " + triple.toString() + " . } WHERE {}";

        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata creation status and location: {}, {}", response.getStatusCode(), location);
        return getContainer(contextUri);
    }

    @Override
    public IRContext deleteMetadata(Triple triple) throws Exception {
        
        logger.debug("Attempting to delete");
        
        FcrepoClient client = buildClient();
        String contextUri = triple.getSubject();

        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);

        String sparql = "DELETE WHERE { " + triple.toString() + " } ";

        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata delete status and location: {}, {}", response.getStatusCode(), location);
        return getContainer(contextUri);
    }

    @Override
    public IRContext updateMetadata(String contextUri, String sparql) throws Exception {
        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(contextUri + "/fcr:metadata"), client);
        patch.body(new ByteArrayInputStream(sparql.getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata update status and location: {}, {}", response.getStatusCode(), location);
        return getContainer(contextUri);
    }

    @Override
    public IRContext createContainer(String contextUri, String name) throws Exception {
        FcrepoClient client = buildClient();
        PostBuilder post = new PostBuilder(new URI(contextUri), client);
        if (!name.isEmpty()) {
            Model model = ModelFactory.createDefaultModel();
            model.createResource("").addProperty(DC.title, name);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            RDFDataMgr.write(out, model, Lang.TURTLE);
            post.body(new ByteArrayInputStream(out.toByteArray()), "text/turtle");
        }
        FcrepoResponse response = post.perform();
        URI location = response.getLocation();
        logger.debug("Container creation status and location: {}, {}", response.getStatusCode(), location);
        return getContainer(contextUri);
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
        return getContainer(contextUri);
    }

    @Override
    public IRContext resourceFixity(Triple tiple) throws Exception {
        String contextUri = tiple.getSubject();
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(URI.create(contextUri + "/fcr:fixity"), client).perform();

        Model model = ModelFactory.createDefaultModel();
        model.read(response.getBody(), null, "text/turtle");
        model.createResource("").addProperty(DC.title, "Fixity Report");

        FixityReport fixityReportContext = FixityReport.of(buildIRContext(model, contextUri).getProperties());
        IRContext thisContext = getContainer(contextUri);
        thisContext.setFixity(fixityReportContext);

        return thisContext;
    }

    @Override
    public IRContext getContainer(String contextUri) throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(contextUri + "/fcr:metadata"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModel(response.getBody());
        return buildIRContext(model, contextUri);
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
        
        if(name.isEmpty()) {
            SimpleDateFormat output = new SimpleDateFormat("yyyyMMddHHmmss");
            name = "version."+output.format(System.currentTimeMillis());
        } else {
            name = "version."+name;
        }
        
        URI uri = URI.create(contextUri+"/fcr:versions");
        logger.info("Attempting to create version: {}", uri.toString());
        FcrepoResponse response = new PostBuilder(uri, buildClient())
                .slug(name)
                .perform();

        URI location = response.getLocation();
        logger.info("Version creation status and location: {}, {}", response.getStatusCode(), location);
        
        return getContainer(contextUri);
    }
    
    @Override
    public IRContext restoreVersion(String contextUri) throws Exception {
        IRContext versionContext = getContainer(contextUri);
        String parentUri = versionContext.getParent().getSubject();
        URI uri = URI.create(contextUri);
        new PatchBuilder(uri, buildClient()).perform();
        return getContainer(parentUri);
    }
    
    @Override
    public void deleteContainer(String uri) throws Exception {
        FcrepoResponse response = new DeleteBuilder(new URI(uri), buildClient()).perform();
        logger.debug("Resource deletion status: {}", response.getStatusCode());
    }

    @Override
    public void setIr(IR ir) {
        this.ir = ir;
    }

    @Override
    public synchronized IRContext buildIRContext(Model model, String contextUri) {
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

        irContext.setName(contextUri.equals(ir.getRootUri()) ? "Root" : contextUri);

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

        return irContext;
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

    private FcrepoClient buildClient() {
        return (ir.getUsername() == null || ir.getPassword() == null) ? FcrepoClient.client().build() : FcrepoClient.client().credentials(ir.getUsername(), ir.getPassword()).build();
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

}
