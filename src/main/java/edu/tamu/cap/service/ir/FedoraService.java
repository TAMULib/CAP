package edu.tamu.cap.service.ir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
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

@Service("Fedora")
public class FedoraService implements IRService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static String LDP_CONTAINS_PREDICATE = "http://www.w3.org/ns/ldp#contains";

	@Override
	public void verifyPing(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
		FcrepoClient client = buildClient(ir);
		FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
		if (response.getStatusCode() == -1) {
			throw new IRVerificationException("No response from " + ir.getRootUri());
		}
	}

	@Override
	public void verifyAuth(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
		FcrepoClient client = buildClient(ir);
		FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
		if (response.getStatusCode() != 200) {
			throw new IRVerificationException("Authenitcation failed. Status " + response.getStatusCode());
		}
	}

	@Override
	public void verifyRoot(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IOException, IRVerificationException {
		FcrepoClient client = buildClient(ir);
		FcrepoResponse response = new GetBuilder(new URI(ir.getRootUri()), client).perform();
		String resBody = IOUtils.toString(response.getBody(), "UTF-8");
		if (!resBody.contains("fedora:RepositoryRoot")) {
			// TODO: Add switch to give better messages by status code
			throw new IRVerificationException("No root found. Status " + response.getStatusCode());
		}
	}

	@Override
	public URI createContainer(IR ir, String name) throws FcrepoOperationFailedException, URISyntaxException { 
		
		FcrepoClient client = buildClient(ir);
		
		PostBuilder post = new PostBuilder(new URI(ir.getContextUri()), client);
		
		if(name != null) {
			Model model = ModelFactory.createDefaultModel();
			model.createResource("")
				.addProperty(DC.title, name);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			RDFDataMgr.write(out, model, Lang.TURTLE);
			post.body(new ByteArrayInputStream(out.toByteArray()), "text/turtle");
		}
		
		FcrepoResponse response = post.perform();
		URI location = response.getLocation();
		logger.debug("Container creation status and location: {}, {}", response.getStatusCode(), location);
		return location;
	}
	
	@Override
	public void deleteContainer(IR ir, String uri) throws Exception {
		FcrepoResponse response = new DeleteBuilder(new URI(uri), buildClient(ir)).perform();
		logger.debug("Resource deletion status: {}", response.getStatusCode());
	}
	
	private FcrepoClient buildClient(IR ir) {
		return (ir.getUsername() == null || ir.getPassword() == null) ? FcrepoClient.client().build() : FcrepoClient.client().credentials(ir.getUsername(), ir.getPassword()).build();
	}

	@Override
	public List<String> getContainers(IR ir) throws Exception {
		FcrepoClient client = buildClient(ir);
		FcrepoResponse response = new GetBuilder(new URI(ir.getContextUri()), client).accept("application/rdf+xml").perform();
		Model model = createRdfModel(response.getBody());

		// model.write(System.out, "JSON-LD");
		// model.write(System.out, "RDF/XML");

		return getObjects(model, LDP_CONTAINS_PREDICATE);
	}
	
	@Override
	public Map<String, List<String>> getMetadata(IR ir) throws Exception {
		FcrepoClient client = buildClient(ir);
		FcrepoResponse response = new GetBuilder(new URI(ir.getContextUri()), client).accept("application/rdf+xml").perform();
		Model model = createRdfModel(response.getBody());
		
		Map<String, List<String>> metadata = new HashMap<String, List<String>>();
		
		model.listStatements().forEachRemaining(statement->{
			
			String label = statement.asTriple().getPredicate().toString();
			String value = statement.asTriple().getObject().toString();
			
			if(metadata.get(label) == null) metadata.put(label, new ArrayList<String>());
			
			metadata.get(label).add(value);
			
		});
		
		return metadata;
	}

	private Model createRdfModel(InputStream stream) {
		Model model = ModelFactory.createDefaultModel();
		model.read(stream, null, "RDF/XML");
		return model;
	}
	
	
	private List<String> getStatements(Model model) {
		List<String> statements = new ArrayList<String>();
		model.listStatements().forEachRemaining(statement->{			
			statements.add(statement.asTriple().getObject().toString());
		});
		
		return statements;	
	}

	private List<String> getObjects(Model model, String predicate) {		
		List<String> children = new ArrayList<String>();
		
		NodeIterator nodeItr = model.listObjectsOfProperty(model.getProperty(predicate));
		while (nodeItr.hasNext()) {
			RDFNode node = nodeItr.next();
			if (node.isResource()) {
				children.add(node.asResource().getURI());
			}
		}
		return children;
	}

}
