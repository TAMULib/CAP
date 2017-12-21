package edu.tamu.cap.service.ir;

import java.net.URI;
import java.util.List;

import edu.tamu.cap.model.IR;

public interface IRService {
	
	public void verifyPing(IR ir) throws Exception;
	public void verifyAuth(IR ir) throws Exception;
	public void verifyRoot(IR ir) throws Exception;
	
	public URI createContainer(IR ir) throws Exception;
	
	public List<String> getContainers(IR ir) throws Exception;

}
