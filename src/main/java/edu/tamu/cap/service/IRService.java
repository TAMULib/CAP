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
    
    // Varafication  Methods
    public void verifyPing() throws Exception;
    public void verifyAuth() throws Exception;
    public void verifyRoot() throws Exception;

    //Containers
    public IRContext createContainer(String contextUri, String name) throws Exception;
    public IRContext getContainer(String contextUri) throws Exception;
    // Currently no update of container
    public void deleteContainer(String targetUri) throws Exception;
    
    //Resources
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception;
    // Currently no read of resource
    // Currently no update of resource
    // Currently not delete of resource
    public IRContext resourceFixity(Triple tiple) throws Exception;
    
    //Metadata
    public IRContext createMetadata(Triple triple) throws Exception;
    //currently no read of metadata
    public IRContext updateMetadata(String contextUri, String sparql) throws Exception;
    public IRContext deleteMetadata(Triple triple) throws Exception;

    //Versions
    public IRContext createVersion(String contextUri, String Name) throws Exception;
    public List<Version> getVersions(String contextUri) throws Exception;
    //currently no update version
    public IRContext restoreVersion(String contextUri) throws Exception;
    public void deleteVersion(String contextUri) throws Exception;
    //Transactions
 
}
