package edu.tamu.cap.service.ir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DC;
import org.apache.tika.Tika;
import org.fcrepo.client.DeleteBuilder;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.fcrepo.client.GetBuilder;
import org.fcrepo.client.PostBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.exceptions.IRVerificationException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

@Service("Fedora")
public class FedoraService implements IRService<Model> {

    private final static String LDP_CONTAINS_PREDICATE = "http://www.w3.org/ns/ldp#contains";

    private final static String FEDORA_BINRAY_PREDICATE = "http://fedora.info/definitions/v4/repository#Binary";

    private final static String EBU_FILENAME_PREDICATE = "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#filename";

    // @formatter:off
	private final static String[] PROPERTY_PREFIXES = new String[] { 
        "http://fedora.info/definitions/v4/repository",
        "http://www.w3.org/1999/02/22-rdf-syntax-ns",
        "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore",
        "http://www.iana.org/assignments/relation",
        "http://www.loc.gov/premis/rdf/v1"
    };
	// @formatter:on

    // @formatter:off
    public final static String[] METADATA_PREFIXES = new String[] {
        "http://purl.org/dc/elements/1.1"
    };
    // @formatter:on

    private Tika tika = new Tika();

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
    public void verifyRoot() throws FcrepoOperationFailedException, URISyntaxException, IOException, IRVerificationException {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
        String resBody = IOUtils.toString(response.getBody(), "UTF-8");
        if (!resBody.contains("fedora:RepositoryRoot")) {
            // TODO: Add switch to give better messages by status code
            throw new IRVerificationException("No root found. Status " + response.getStatusCode());
        }
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

        InputStream fileStream = new ByteArrayInputStream(file.getBytes());
        post.body(fileStream, tika.detect(fileStream));
        post.filename(file.getOriginalFilename());
        FcrepoResponse response = post.perform();
        URI location = response.getLocation();
        logger.debug("Resource creation status and location: {}, {}", response.getStatusCode(), location);
        return getContainer(contextUri);
    }

    @Override
    public IRContext getContainer(String contextUri) throws Exception {
        FcrepoClient client = buildClient();
        FcrepoResponse response = new GetBuilder(new URI(contextUri + "/fcr:metadata"), client).accept("application/rdf+xml").perform();
        Model model = createRdfModel(response.getBody());
        return buildIRContext(model);
    }

    @Override
    public IRContext updateContainer(String contextUri) throws Exception {
        // TODO: do this
        return null;
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
    public synchronized IRContext buildIRContext(Model model) {
        IRContext irContext = new IRContext();

        // System.out.println("\n::\n");
        model.listStatements().forEachRemaining(statement -> {

            Triple triple = craftTriple(statement);

            // System.out.println();
            // System.out.println(" SUBJECT: " + triple.getSubject());
            // System.out.println(" PREDICATE: " + triple.getPredicate());
            // System.out.println(" OBJECT: " + triple.getObject());
            // System.out.println();

            String predicate = triple.getPredicate();

            switch (predicate) {
            case LDP_CONTAINS_PREDICATE:
                irContext.addChild(new IRContext(triple));
                break;
            default:

                for (String prefix : PROPERTY_PREFIXES) {
                    if (predicate.startsWith(prefix)) {
                        irContext.addProperty(triple);
                        if (triple.getObject().equals(FEDORA_BINRAY_PREDICATE)) {
                            irContext.setResource(true);
                        }
                    }
                }

                for (String prefix : METADATA_PREFIXES) {
                    if (predicate.startsWith(prefix)) {
                        irContext.addMetadum(triple);
                    }
                }
            }

        });
        // System.out.println("\n\n");

        irContext.setName(getName(model, irContext.isResource() ? model.createProperty(EBU_FILENAME_PREDICATE) : DC.title));
        irContext.setTriple(getTriple(model));

        return irContext;
    }

    private Triple craftTriple(Statement statement) {
        String subject = statement.asTriple().getSubject().toString();
        String predicate = statement.asTriple().getPredicate().toString();
        String object = statement.asTriple().getObject().toString();
        return Triple.of(subject, predicate, object);
    }

    private String getName(Model model, Property prop) {
        Optional<String> name = Optional.empty();
        ResIterator titleResources = model.listResourcesWithProperty(prop);
        while (titleResources.hasNext()) {
            name = Optional.of(titleResources.next().getProperty(prop).asTriple().getObject().toString());
            break;
        }
        if (!name.isPresent()) {
            StmtIterator statements = model.listStatements();
            while (statements.hasNext()) {
                String subject = statements.next().asTriple().getSubject().toString();
                name = Optional.of(subject.equals(ir.getRootUri()) ? "Root" : subject);
                break;
            }
        }
        return name.get();
    }

    private Triple getTriple(Model model) {
        Optional<Triple> triple = Optional.empty();
        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            triple = Optional.of(craftTriple(statements.next()));
            break;
        }
        return triple.get();
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
