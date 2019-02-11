package edu.tamu.cap.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.response.RVContext;
import edu.tamu.cap.model.response.Triple;

public interface RVService<M> {

    public void setRv(RV rv);

    public RVContext buildRVContext(M model, String contextUri) throws Exception;

    public RVContext getRVContext(String contextUri) throws Exception;
    
    public List<Triple> getTriples(RVService<?> rvService, String contextUri) throws Exception;
    
    // Children
    public RVContext createChild(String contextUri, List<Triple> metadata) throws Exception;
    
    public void deleteRVContext(String contextUri) throws Exception;
    
    public List<Triple> getChildren(String contextUri) throws Exception;

    // Resources
    public RVContext createResource(String contextUri, MultipartFile file) throws Exception;
    
    public RVContext getResource(String contextUri) throws Exception;
    
    public void deleteResource(String contextUri) throws Exception;
    
    // Metadata
    public RVContext createMetadata(String contextUri, Triple triple) throws Exception;
    
    public List<Triple> getMetadata(String contextUri) throws Exception;
    
    public RVContext updateMetadata(String contextUri, Triple triple, String newValue) throws Exception;
    
    public RVContext deleteMetadata(String contextUri, Triple triple) throws Exception;
    
    public RV getRV();
    
    public default RVContext featureSupport(RVContext context) {  
        for(Class<?> i : this.getClass().getInterfaces()) {
            if(!i.getSimpleName().equals("RVService")) {
                context.addFeature(i.getSimpleName().replace("RVService", "").toLowerCase(), true);
            }
        }
        return context;
    }

}
