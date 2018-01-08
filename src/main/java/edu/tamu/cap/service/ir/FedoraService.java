package edu.tamu.cap.service.ir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DC;
import org.fcrepo.client.DeleteBuilder;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.fcrepo.client.GetBuilder;
import org.fcrepo.client.PostBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.tamu.cap.exceptions.IRVerificationException;
import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

@Service("Fedora")
public class FedoraService implements IRService<Model> {

    private final static String LDP_CONTAINS_PREDICATE = "http://www.w3.org/ns/ldp#contains";

    private final static String W3_TYPE_PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    private final static String FEDORA_BINRAY_PREDICATE = "http://fedora.info/definitions/v4/repository#Binary";

    // @formatter:off
    private final static List<String> FEDORA_PROPERTY_PREDICATES = Arrays.asList(new String[] {
         "http://fedora.info/definitions/v4/2016/10/18/repository#hasParent",
         "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
         "http://fedora.info/definitions/v4/repository#writable",
         "http://fedora.info/definitions/v4/repository#lastModified",
         "http://fedora.info/definitions/v4/repository#created",
         "http://fedora.info/definitions/v4/repository#lastModifiedBy",
         "http://fedora.info/definitions/v4/repository#createdBy"
    });
    // @formatter:on

    // @formatter:off
    private final static List<String> DUBLIN_CORE_METADATA_PREDICATES = Arrays.asList(new String[] {
        DC.contributor.getURI(),
        DC.coverage.getURI(),
        DC.creator.getURI(),
        DC.date.getURI(),
        DC.description.getURI(),
        DC.format.getURI(),
        DC.identifier.getURI(),
        DC.language.getURI(),
        DC.publisher.getURI(),
        DC.relation.getURI(),
        DC.rights.getURI(),
        DC.source.getURI(),
        DC.subject.getURI(),
        DC.title.getURI(),
        DC.type.getURI(),
        DC.contributor.getURI(),
        DC.contributor.getURI()
    });
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
    public IRContext buildIRContext(Model model) {
        IRContext irContext = new IRContext();
        irContext.setName(getName(model));
        irContext.setTriple(getTriple(model));

        model.listStatements().forEachRemaining(statement -> {

            Triple triple = craftTriple(statement);

            String predicate = triple.getPredicate();

            switch (predicate) {
            case LDP_CONTAINS_PREDICATE:
                irContext.addChild(new IRContext(triple));
                break;
            case W3_TYPE_PREDICATE:
                if (triple.getObject().equals(FEDORA_BINRAY_PREDICATE)) {
                    irContext.setResource(true);
                }
                break;
            default:
                // TODO: figure out how to get fedora properties
                if (FEDORA_PROPERTY_PREDICATES.contains(predicate)) {

                }

                irContext.addProperty(triple);

                if (DUBLIN_CORE_METADATA_PREDICATES.contains(predicate)) {
                    irContext.addMetadum(triple);
                }
            }

        });

        return irContext;
    }

    private Triple craftTriple(Statement statement) {
        String predicate = statement.asTriple().getPredicate().toString();
        String subject = statement.asTriple().getSubject().toString();
        String object = statement.asTriple().getObject().toString();

        Triple triple = new Triple();
        triple.setSubject(subject);
        triple.setPredicate(predicate);
        triple.setObject(object);
        return triple;
    }

    private String getName(Model model) {
        Optional<String> name = Optional.empty();
        ResIterator titleResources = model.listResourcesWithProperty(DC.title);
        while (titleResources.hasNext()) {
            name = Optional.of(titleResources.next().getProperty(DC.title).asTriple().getObject().toString());
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

        // model.write(System.out, "JSON-LD");
        // model.write(System.out, "RDF/XML");

        return model;
    }

}
