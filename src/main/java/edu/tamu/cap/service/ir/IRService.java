package edu.tamu.cap.service.ir;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.tamu.cap.model.IR;

public interface IRService {
	
	public void verifyPing(IR ir) throws Exception;
	public void verifyAuth(IR ir) throws Exception;
	public void verifyRoot(IR ir) throws Exception;
	
	public URI createContainer(IR ir, String name) throws Exception;
	public void deleteContainer(IR ir, String targetUri) throws Exception;
	public List<String> getContainers(IR ir) throws Exception;
	
	public Map<String, List<String>> getMetadata(IR ir) throws Exception;

}
