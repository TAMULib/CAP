package edu.tamu.cap.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;

public interface IRService<M> {

    public void setIr(IR ir);

    public IRContext buildIRContext(M model, String contextUri);

    // Verification Methods
    public void verifyPing() throws Exception;

    public void verifyAuth() throws Exception;

    public void verifyRoot() throws Exception;

    public IRContext getIRContext(String contextUri) throws Exception;
    
    public List<Triple> getTriples(IRService<?> irService, String contextUri) throws Exception;
    
    // Children
    public IRContext createChild(String contextUri, List<Triple> metadata) throws Exception;
    public List<Triple> getChildren(String contextUri) throws Exception;

    // TODO: currently no update of container
    public void deleteContainer(String targetUri) throws Exception;

    // Resources
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception;

    // TODO: currently no read of resource
    // TODO: currently no update of resource
    // TODO: currently not delete of resource
    public IRContext resourceFixity(Triple tiple) throws Exception;

    // Metadata
    public IRContext createMetadata(Triple triple) throws Exception;
    
    public List<Triple> getMetadata(String contextUri) throws Exception;
    
    public IRContext updateMetadata(Triple triple, String newValue) throws Exception;
    
    public IRContext deleteMetadata(Triple triple) throws Exception;

    // Versions
    public IRContext createVersion(String contextUri, String Name) throws Exception;

    public List<Version> getVersions(String contextUri) throws Exception;

    // TODO: currently no update version
    public IRContext restoreVersion(String contextUri) throws Exception;

    public void deleteVersion(String contextUri) throws Exception;

    // Transactions

}
