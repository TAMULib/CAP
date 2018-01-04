package edu.tamu.cap.service.ir;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.tamu.cap.model.IR;

public interface IRService {
	
	public void verifyPing() throws Exception;
	public void verifyAuth() throws Exception;
	public void verifyRoot() throws Exception;
	
	public URI createContainer(String contextUri, String name) throws Exception;
	public void deleteContainer(String targetUri) throws Exception;
	public List<String> getContainers(String contextUri) throws Exception;
	
	public Map<String, List<String>> getMetadata() throws Exception;
	
	public void setIr(IR ir);

}
