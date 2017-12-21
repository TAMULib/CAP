package edu.tamu.cap.service.ir;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.fcrepo.client.FcrepoClient;
import org.fcrepo.client.FcrepoOperationFailedException;
import org.fcrepo.client.FcrepoResponse;
import org.fcrepo.client.GetBuilder;
import org.springframework.stereotype.Service;

import edu.tamu.cap.exceptions.IRVerificationException;
import edu.tamu.cap.model.IR;

@Service("Fedora")
public class FedoraService implements IRService {

	@Override
	public void verifyPing(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
		FcrepoClient client = FcrepoClient.client().build();
		FcrepoResponse response = new GetBuilder(new URI(ir.getUri()), client).perform();
		if (response.getStatusCode() == -1) {
			throw new IRVerificationException("No response from " + ir.getUri());
		}
	}

	@Override
	public void verifyAuth(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IRVerificationException {
		FcrepoClient client = FcrepoClient.client().credentials(ir.getUsername(), ir.getPassword()).build();
		FcrepoResponse response = new GetBuilder(new URI(ir.getUri()), client).perform();
		if (response.getStatusCode() != 200) {
			throw new IRVerificationException("Authenitcation failed. Status " + response.getStatusCode());
		}
	}

	@Override
	public void verifyRoot(IR ir) throws FcrepoOperationFailedException, URISyntaxException, IOException, IRVerificationException {
		FcrepoClient client = (ir.getUsername() == null || ir.getPassword() == null) ? FcrepoClient.client().build() : FcrepoClient.client().credentials(ir.getUsername(), ir.getPassword()).build();
		FcrepoResponse response = new GetBuilder(new URI(ir.getUri()), client).perform();
		String resBody = IOUtils.toString(response.getBody(), "UTF-8");
		if (!resBody.contains("fedora:RepositoryRoot")) {
			//TODO: Add switch to give better messages by status code
			throw new IRVerificationException("No root found. Status " + response.getStatusCode());
		}
	}

}
