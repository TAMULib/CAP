package edu.tamu.cap.service.repositoryview;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;

public interface RepositoryViewService<M> {
    
    // NOTE: if additional interface method is introduced and needs to broadcast, update pointcut in ContextBroadcastAspect

    public void setRepositoryView(RepositoryView rv);

    public RepositoryViewContext buildRepositoryViewContext(M model, String contextUri) throws Exception;

    public RepositoryViewContext getRepositoryViewContext(String contextUri) throws Exception;

    public List<Triple> getTriples(RepositoryViewService<?> rvService, String contextUri) throws Exception;

    // Children
    public RepositoryViewContext createChild(String contextUri, List<Triple> metadata) throws Exception;

    public void deleteRepositoryViewContext(String contextUri) throws Exception;

    public List<Triple> getChildren(String contextUri) throws Exception;

    // Resources
    public RepositoryViewContext createResource(String contextUri, MultipartFile file) throws Exception;

    public RepositoryViewContext getResource(String contextUri) throws Exception;

    public void deleteResource(String contextUri) throws Exception;

    // Metadata
    public RepositoryViewContext createMetadata(String contextUri, Triple triple) throws Exception;

    public List<Triple> getMetadata(String contextUri) throws Exception;

    public RepositoryViewContext updateMetadata(String contextUri, Triple triple, String newValue) throws Exception;

    public RepositoryViewContext deleteMetadata(String contextUri, Triple triple) throws Exception;

    public RepositoryView getRepositoryView();

    public default RepositoryViewContext featureSupport(RepositoryViewContext context) {
        for (Class<?> i : this.getClass().getInterfaces()) {
            if (!i.getSimpleName().equals("RepositoryViewService")) {
                context.addFeature(i.getSimpleName().replace("RepositoryViewService", "").toLowerCase(), true);
            }
        }
        return context;
    }

}
