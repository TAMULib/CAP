package edu.tamu.cap.service.ir;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;

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

}
