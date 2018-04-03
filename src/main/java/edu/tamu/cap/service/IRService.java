package edu.tamu.cap.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

public interface IRService<M> {

    public void setIr(IR ir);

    public IRContext buildIRContext(M model, String contextUri);

    public IRContext getIRContext(String contextUri) throws Exception;
    
    public List<Triple> getTriples(IRService<?> irService, String contextUri) throws Exception;
    
    // Children
    public IRContext createChild(String contextUri, List<Triple> metadata) throws Exception;
    
    public void deleteIRContext(String targetUri) throws Exception;
    
    public List<Triple> getChildren(String contextUri) throws Exception;

    // Resources
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception;
    
    public IRContext getResource(String contextUri) throws Exception;
    
    public void deleteResource(String contextUri) throws Exception;
    
    public IRContext resourceFixity(Triple tiple) throws Exception;

    // Metadata
    public IRContext createMetadata(Triple triple) throws Exception;
    
    public List<Triple> getMetadata(String contextUri) throws Exception;
    
    public IRContext updateMetadata(Triple triple, String newValue) throws Exception;
    
    public IRContext deleteMetadata(Triple triple) throws Exception;
    
    public default IRContext featureSupport(IRContext context) {  
        for(Class<?> i : this.getClass().getInterfaces()) {
            if(!i.getSimpleName().equals("IRService")) {
                context.addFeature(i.getSimpleName().replace("IRService", "").toLowerCase(), true);
            }
        }
        return context;
    }

}
