package edu.tamu.cap.service.ir;

import java.net.URI;
import java.net.URISyntaxException;

import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.GetBuilder;
import org.springframework.stereotype.Service;

import edu.tamu.cap.model.IR;

@Service()
public class FedoraService implements IRService {

	public void ping(IR ir) throws FcrepoOperationFailedException, URISyntaxException {
		FcrepoClient client = FcrepoClient.client().build();
    	new GetBuilder(new URI(ir.getUri()), client).perform();
	}
	
}
