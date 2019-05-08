package edu.tamu.cap.service;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Triple;

@Service("DSpace")
public class DSpaceService implements RepositoryViewService<Model> {

    @Override
    public RepositoryViewContext getRepositoryViewContext(String contextUri) throws Exception {

        return null;
    }

    @Override
    public List<Triple> getTriples(RepositoryViewService<?> repositoryViewService, String contextUri) throws Exception {
        return null;
    }

    @Override
    public RepositoryViewContext createChild(String contextUri, List<Triple> metadata) throws Exception {
        return null;
    }

    @Override
    public RepositoryViewContext createResource(String contextUri, MultipartFile file) throws Exception {

        return null;
    }

    @Override
    public RepositoryViewContext createMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

    @Override
    public RepositoryViewContext updateMetadata(String contextUri, Triple originalTriple, String query) throws Exception {

        return null;
    }

    @Override
    public void setRepositoryView(RepositoryView repositoryView) {

    }

    @Override
    public RepositoryViewContext buildRepositoryViewContext(Model model, String contextUri) {

        return null;
    }

    @Override
    public RepositoryViewContext deleteMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

    @Override
    public List<Triple> getMetadata(String contextUri) throws Exception {
        return null;
    }

    @Override
    public List<Triple> getChildren(String contextUri) throws Exception {
        return null;
    }

    @Override
    public RepositoryViewContext getResource(String contextUri) throws Exception {
        return null;
    }

    @Override
    public void deleteResource(String contextUri) throws Exception {

    }

    @Override
    public void deleteRepositoryViewContext(String targetUri) throws Exception {

    }

    public RepositoryView getRepositoryView() {
        return null;
    }

}