package edu.tamu.cap.service;

import static edu.tamu.cap.utility.ContextUtility.buildFullContextURI;
import static edu.tamu.cap.utility.ContextUtility.getTransactionToken;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDFS;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.tamu.cap.exceptions.RepositoryViewVerificationException;
import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.response.FixityReport;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.TransactionDetails;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;

@Service("Fedora")
@Scope(value = SCOPE_REQUEST)
public class FedoraService implements RepositoryViewService<Model>, VersioningRepositoryViewService<Model>, VerifyingRepositoryViewService<Model>, TransactingRepositoryViewService<Model>, QueryableRepositoryViewService<Model>, FixityRepositoryViewService<Model> {

    private final static String LDP_CONTAINS_PREDICATE = "http://www.w3.org/ns/ldp#contains";

    private final static String RDF_TYPE_PREDICATE = "https://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    private final static String EBU_FILENAME_PREDICATE = "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#filename";

    private final static String FEDORA_ROOT_PREDICATE = "http://fedora.info/definitions/v4/repository#RepositoryRoot";

    private final static String FEDORA_HAS_PARENT_PREDICATE = "http://fedora.info/definitions/v4/repository#hasParent";

    private final static String FEDORA_CONTAINER_PREDICATE = "http://fedora.info/definitions/v4/repository#Container";

    public final static String FEDORA_BINARY_PREDICATE = "http://fedora.info/definitions/v4/repository#Binary";

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
    private TransactionService transactionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RepositoryView repositoryView;

    @Override
    public void verifyPing() throws FcrepoOperationFailedException, URISyntaxException, RepositoryViewVerificationException {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(repositoryView.getRootUri()), client).perform();
        if (response.getStatusCode() == -1) {
            throw new RepositoryViewVerificationException("No response from " + repositoryView.getRootUri());
        }
    }

    @Override
    public void verifyAuth() throws FcrepoOperationFailedException, URISyntaxException, RepositoryViewVerificationException {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(repositoryView.getRootUri()), client).perform();
        if (response.getStatusCode() != 200) {
            throw new RepositoryViewVerificationException("Authenitcation failed. Status " + response.getStatusCode());
        }
    }

    @Override
    public void verifyRoot() throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(repositoryView.getRootUri() + "/fcr:metadata"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModelFromResponse(response);
        Optional<String> root = getLiteralForProperty(model, model.createProperty(FEDORA_ROOT_PREDICATE));
        if (root.isPresent()) {
            throw new RepositoryViewVerificationException("URI is not a Fedora root!");
        }
    }

    @Override
    public RepositoryViewContext getRepositoryViewContext(String contextUri) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);

        FcrepoClient client = buildClient();

        FcrepoResponse response = new GetBuilder(new URI(longContextUri + "/fcr:metadata"), client).accept("application/rdf+xml").perform();

        Model model = createRdfModelFromResponse(response);
        RepositoryViewContext context = buildRepositoryViewContext(model, longContextUri);

        Optional<String> transactionToken = getTransactionToken(longContextUri);

        if (transactionToken.isPresent()) {
            Optional<ZonedDateTime> tokenExpiration = transactionService.get(transactionToken.get());
            if (tokenExpiration.isPresent()) {
                context.setTransactionDetails(TransactionDetails.of(transactionToken.get(), tokenExpiration.get()));
            }
        }

        return context;
    }

    private Model createRdfModelFromResponse(FcrepoResponse response) throws Exception {
        checkFedoraResult(response);
        return createRdfModel(response.getBody());
    }

    private void checkFedoraResult(FcrepoResponse response) throws Exception {
        if (response.getStatusCode() > 399) {
            throw new FcrepoOperationFailedException(response.getUrl(), response.getStatusCode(), "Error response from fedora: " + response.getStatusCode());
        }
    }

    @Override
    public void deleteRepositoryViewContext(String contextUri) throws Exception {
        logger.info("Deletion or contianer: {}", contextUri);
        delete(contextUri);
    }

    @Override
    public List<Triple> getTriples(RepositoryViewService<?> repositoryViewService, String contextUri) throws Exception {
        RepositoryViewContext context = getRepositoryViewContext(contextUri);

        List<Triple> allTriples = new ArrayList<Triple>();

        allTriples.addAll(context.getProperties());
        allTriples.addAll(context.getMetadata());
        // TODO add all resource triples

        return allTriples;
    }

    @Override
    public RepositoryViewContext createChild(String contextUri, List<Triple> metadata) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        PostBuilder post = new PostBuilder(new URI(longContextUri), client);

        Model model = ModelFactory.createDefaultModel();
        metadata.forEach(metadatum -> {
            Property prop = model.createProperty(metadatum.getPredicate());
            model.createResource("").addProperty(prop, metadatum.getObject());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            RDFDataMgr.write(out, model, Lang.TURTLE);
            post.body(new ByteArrayInputStream(out.toByteArray()), "text/turtle");
        });

        FcrepoResponse response = post.perform();
        URI location = response.getLocation();
        logger.debug("Container creation status and location: {}, {}", response.getStatusCode(), location);
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public List<Triple> getChildren(String contextUri) throws Exception {
        List<Triple> childrenTriples = new ArrayList<Triple>();
        getRepositoryViewContext(contextUri).getChildren().forEach(childContext -> {
            childrenTriples.add(childContext.getTriple());
        });
        return childrenTriples;
    }

    @Override
    public RepositoryViewContext createMetadata(String contextUri, Triple triple) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        triple.setSubject(buildFullContextURI(repositoryView.getRootUri(), triple.getSubject()));
        triple.validate();

        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(longContextUri + "/fcr:metadata"), client);
        String sparql = "INSERT { " + triple.toString() + " . } WHERE {}";
        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        URI location = response.getLocation();
        logger.debug("Metadata creation status and location: {}, {}", response.getStatusCode(), location);
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public List<Triple> getMetadata(String contextUri) throws Exception {
        RepositoryViewContext context = getRepositoryViewContext(contextUri);
        return context.getMetadata();
    }

    @Override
    public RepositoryViewContext updateMetadata(String contextUri, Triple originalTriple, String value) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        StringBuilder stngBldr = new StringBuilder();

        Triple newTriple = new Triple(originalTriple.getSubject(), originalTriple.getPredicate(), value);
        originalTriple.validate();
        newTriple.validate();

        stngBldr.append("DELETE { <> <").append(originalTriple.getPredicate()).append("> ").append(originalTriple.getObject()).append(" } ");
        stngBldr.append("INSERT { <> <").append(originalTriple.getPredicate()).append("> ").append(value).append(" } ");
        stngBldr.append("WHERE { }");
        String sparql = stngBldr.toString();

        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(longContextUri + "/fcr:metadata"), client);
        patch.body(new ByteArrayInputStream(sparql.getBytes()));

        FcrepoResponse response = patch.perform();
        checkFedoraResult(response);
        logger.debug("Metadata update status and location: {}, {}", response.getStatusCode(), response.getLocation());

        return getRepositoryViewContext(contextUri);
    }

    @Override
    public RepositoryViewContext deleteMetadata(String contextUri, Triple triple) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        triple.setSubject(buildFullContextURI(repositoryView.getRootUri(), triple.getSubject()));
        triple.validate();

        logger.debug("Attempting to delete");

        FcrepoClient client = buildClient();

        PatchBuilder patch = new PatchBuilder(new URI(longContextUri + "/fcr:metadata"), client);

        String sparql = "DELETE WHERE { " + triple.toString() + " } ";

        UpdateRequest request = UpdateFactory.create();

        request.add(sparql);

        patch.body(new ByteArrayInputStream(request.toString().getBytes()));

        FcrepoResponse response = patch.perform();
        checkFedoraResult(response);
        URI location = response.getLocation();
        logger.debug("Metadata delete status and location: {}, {}", response.getStatusCode(), location);
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public RepositoryViewContext createResource(String contextUri, MultipartFile file) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        PostBuilder post = new PostBuilder(new URI(longContextUri), client);
        post.body(file.getInputStream(), file.getContentType());
        post.filename(file.getOriginalFilename());
        FcrepoResponse response = post.perform();
        checkFedoraResult(response);
        URI location = response.getLocation();
        logger.debug("Resource creation status and location: {}, {}", response.getStatusCode(), location);
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public RepositoryViewContext getResource(String contextUri) throws Exception {
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public void deleteResource(String contextUri) throws Exception {
        logger.info("Deleting resource: {}", contextUri);
        delete(contextUri);
    }

    @Override
    public FixityReport resourceFixity(String contextUri) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(URI.create(longContextUri + "/fcr:fixity"), client).perform();

        Model model = ModelFactory.createDefaultModel();
        model.read(response.getBody(), null, "text/turtle");
        model.createResource("").addProperty(DC.title, "Fixity Report");

        FixityReport fixityReportContext = FixityReport.of(buildRepositoryViewContext(model, longContextUri).getProperties());

        return fixityReportContext;
    }

    @Override
    public List<Version> getVersions(String contextUri) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(longContextUri + "/fcr:versions"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModelFromResponse(response);
        List<Version> versions = new ArrayList<Version>();

        model.listObjectsOfProperty(model.createProperty(FEDORA_HAS_VERSION_PREDICATE)).forEachRemaining(object -> {

            Version version = Version.of(Triple.of(longContextUri + "/fcr:versions", FEDORA_HAS_VERSION_PREDICATE, object.toString()));

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
    public RepositoryViewContext createVersion(String contextUri, String name) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);

        if (name.isEmpty()) {
            SimpleDateFormat output = new SimpleDateFormat("yyyyMMddHHmmss");
            name = "version." + output.format(System.currentTimeMillis());
        } else {
            name = "version." + name;
        }

        URI uri = URI.create(longContextUri + "/fcr:versions");
        logger.info("Attempting to create version: {}", uri.toString());

        FcrepoResponse response = new PostBuilder(uri, buildClient()).slug(name).perform();
        checkFedoraResult(response);
        URI location = response.getLocation();
        logger.info("Version creation status and location: {}, {}", response.getStatusCode(), location);

        return getRepositoryViewContext(contextUri);
    }

    @Override
    public RepositoryViewContext restoreVersion(String contextUri) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        RepositoryViewContext versionContext = getRepositoryViewContext(contextUri);
        String parentUri = versionContext.getParent().getSubject();
        URI uri = URI.create(longContextUri);
        new PatchBuilder(uri, buildClient()).perform();
        return getRepositoryViewContext(parentUri);
    }

    @Override
    public void deleteVersion(String contextUri) throws Exception {
        logger.info("Deleting version: {}", contextUri);
        delete(contextUri);
    }

    @Override
    public TransactionDetails startTransaction() throws Exception {
        FcrepoClient client = buildClient();

        URI transactionContextURI = new URI(repositoryView.getRootUri() + "fcr:tx");

        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();

        String fedoraDate = response.getHeaderValue("Expires");

        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss z");
        ZonedDateTime expirationDate = ZonedDateTime.parse(fedoraDate, f);

        logger.debug("Transaction Start: {}", transactionContextURI);

        Optional<String> token = getTransactionToken(response.getLocation().toString());

        transactionService.add(token.get(), Duration.ofMinutes(3));

        return makeTransactionDetails(token.get(), DateTimeFormatter.ISO_ZONED_DATE_TIME.format(expirationDate));
    }

    @Override
    public TransactionDetails refreshTransaction(String tokenURI) throws Exception {
        FcrepoClient client = buildClient();

        URI transactionContextURI = new URI(tokenURI + "/fcr:tx");

        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();

        String fedoraDate = response.getHeaderValue("Expires");

        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss z");
        ZonedDateTime expirationDate = ZonedDateTime.parse(fedoraDate, f);

        String location = response.getLocation() != null ? response.getLocation().toString() : tokenURI;

        logger.debug("Transaction Refresh: {}", transactionContextURI);

        Optional<String> token = getTransactionToken(location);

        transactionService.add(token.get(), Duration.ofMinutes(3));

        return makeTransactionDetails(token.get(), DateTimeFormatter.ISO_ZONED_DATE_TIME.format(expirationDate));
    }

    @Override
    public void commitTransaction(String tokenURI) throws Exception {
        FcrepoClient client = buildClient();

        URI transactionContextURI = new URI(tokenURI + "/fcr:tx/fcr:commit");

        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();

        logger.debug("Transaction Commited: {}", response.getStatusCode());
    }

    @Override
    public void rollbackTransaction(String tokenURI) throws Exception {
        FcrepoClient client = buildClient();

        URI transactionContextURI = new URI(tokenURI + "/fcr:tx/fcr:rollback");

        FcrepoResponse response = new PostBuilder(transactionContextURI, client).perform();

        logger.debug("Transaction RollBack: {}", response.getStatusCode());
    }

    @Override
    public TransactionDetails makeTransactionDetails(String transactionToken, String expirationString) throws Exception {
        return TransactionDetails.of(transactionToken, expirationString);
    }

    @Override
    public void setRepositoryView(RepositoryView repositoryView) {
        this.repositoryView = repositoryView;
    }

    @Override
    public RepositoryViewContext query(String contextUri, String sparql) throws Exception {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        PatchBuilder patch = new PatchBuilder(new URI(longContextUri + "/fcr:metadata"), client);
        patch.body(new ByteArrayInputStream(sparql.getBytes()));

        FcrepoResponse response = patch.perform();
        checkFedoraResult(response);
        URI location = response.getLocation();
        logger.debug("Advanced update query status and location: {}, {}", response.getStatusCode(), location);
        return getRepositoryViewContext(contextUri);
    }

    @Override
    public String getQueryHelp() throws Exception {
        StringBuilder strbldr = new StringBuilder();

        repositoryView.getSchemas().forEach(schema -> {
            strbldr.append("PREFIX ").append(schema.getAbbreviation()).append(": <").append(schema.getNamespace()).append(">\n");
        });

        strbldr.append("\n\n");
        strbldr.append("DELETE { }\n");
        strbldr.append("INSERT { }\n");
        strbldr.append("WHERE { }\n\n");

        return strbldr.toString();
    }

    @Override
    public RepositoryView getRepositoryView() {
        return repositoryView;
    }

    @Override
    public synchronized RepositoryViewContext buildRepositoryViewContext(Model model, String longContextUri) throws JsonProcessingException, UnsupportedEncodingException, IOException {

        RepositoryViewContext repositoryViewContext = new RepositoryViewContext(Triple.of(longContextUri, RDF_TYPE_PREDICATE, FEDORA_CONTAINER_PREDICATE));

        // System.out.println("\n::\n");
        model.listStatements().forEachRemaining(statement -> {

            Triple triple = Triple.fromJenaTriple(statement.asTriple());

            // System.out.println();
            // System.out.println(" SUBJECT: " + triple.getSubject());
            // System.out.println(" PREDICATE: " + triple.getPredicate());
            // System.out.println(" OBJECT: " + triple.getObject());
            // System.out.println();

            {
                String predicate = triple.getPredicate();

                switch (predicate) {
                case FEDORA_HAS_PARENT_PREDICATE:
                    repositoryViewContext.setParent(triple);
                    break;
                case LDP_CONTAINS_PREDICATE:
                    repositoryViewContext.addChild(new RepositoryViewContext(triple));
                    break;
                case FEDORA_HAS_VERSIONS_PREDICATE:
                    try {
                        repositoryViewContext.setVersions(getVersions(triple.getSubject()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    boolean unassociatedPredicate = true;

                    for (String prefix : PROPERTY_PREFIXES) {
                        if (predicate.startsWith(prefix)) {
                            repositoryViewContext.addProperty(triple);
                            unassociatedPredicate = false;
                            break;
                        }
                    }

                    for (String prefix : repositoryView.getMetadataPrefixes()) {
                        if (predicate.startsWith(prefix)) {
                            repositoryViewContext.addMetadatum(triple);
                            unassociatedPredicate = false;
                            break;
                        }
                    }

                    if (unassociatedPredicate) {
                        repositoryViewContext.addProperty(triple);
                    }
                }
            }

            {
                String object = triple.getObject();

                switch (object) {
                case FEDORA_BINARY_PREDICATE:
                    repositoryViewContext.getTriple().setObject(FEDORA_BINARY_PREDICATE);
                    break;
                case FEDORA_VERSION:

                    String verionName = longContextUri.substring(longContextUri.lastIndexOf("/") + 1, longContextUri.length());
                    repositoryViewContext.setVersion(verionName);

                    repositoryViewContext.setParent(Triple.of(object, FEDORA_HAS_PARENT_PREDICATE, longContextUri.replace("/fcr:versions/" + verionName, "")));

                    break;
                default:

                }
            }

        });

        if (longContextUri.equals(repositoryView.getRootUri())) {
            repositoryViewContext.setName("Root");
        } else {
            repositoryViewContext.setName(longContextUri);
        }

        Optional<String> title = getLiteralForProperty(model, RDFS.label);
        if (!title.isPresent()) {
            title = getLiteralForProperty(model, DC.title);

            if (!title.isPresent()) {
                if (repositoryViewContext.isResource()) {
                    title = getLiteralForProperty(model, model.createProperty(EBU_FILENAME_PREDICATE));
                }
            }
        }

        if (title.isPresent()) {
            repositoryViewContext.setName(title.get());
        }

        if (repositoryViewContext.getIsVersion()) {
            repositoryViewContext.setName(repositoryViewContext.getName() + " (" + repositoryViewContext.getVersion() + ")");
        }

        return featureSupport(repositoryViewContext);
    }

    private FcrepoClient buildClient() {
        return (repositoryView.getUsername() == null || repositoryView.getPassword() == null) ? FcrepoClient.client().build() : FcrepoClient.client().credentials(repositoryView.getUsername(), repositoryView.getPassword()).build();
    }

    private FcrepoResponse delete(String contextUri) throws URISyntaxException, FcrepoOperationFailedException {
        String longContextUri = buildFullContextURI(repositoryView.getRootUri(), contextUri);
        FcrepoClient client = buildClient();
        URI newURI = new URI(longContextUri);
        DeleteBuilder builder = new DeleteBuilder(newURI, client);
        return builder.perform();
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

}
