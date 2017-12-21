package edu.tamu.cap.service.ir;

import java.io.IOException;
import java.net.URISyntaxException;

import org.fcrepo.client.FcrepoOperationFailedException;

import edu.tamu.cap.model.IR;

public interface IRService {
	
	public void verifyPing(IR ir) throws Exception;
	public void verifyAuth(IR ir) throws Exception;
	public void verifyRoot(IR ir) throws Exception;

}
