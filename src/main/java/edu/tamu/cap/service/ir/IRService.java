package edu.tamu.cap.service.ir;

import java.net.URISyntaxException;

import org.fcrepo.client.FcrepoOperationFailedException;

import edu.tamu.cap.model.IR;

public interface IRService {
	public void ping(IR ir) throws FcrepoOperationFailedException, URISyntaxException;
}
