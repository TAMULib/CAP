package edu.tamu.cap.service.ir;

import java.util.List;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

public interface IRService<M> {

	public void verifyPing() throws Exception;

	public void verifyAuth() throws Exception;

	public void verifyRoot() throws Exception;

	public IRContext createContainer(String contextUri, String name) throws Exception;

	public IRContext getContainer(String contextUri) throws Exception;

	public IRContext updateContainer(String contextUri) throws Exception;

	public void deleteContainer(String targetUri) throws Exception;

	public void setIr(IR ir);

	public IRContext buildIRContext(M model);

	public String getName(M model);
	
	public boolean isResource(M model);

	public List<Triple> getProperties(M model);

	public List<Triple> getMetadata(M model);

	public List<Triple> getContainers(M model);

	public List<Triple> getResources(M model);

}
