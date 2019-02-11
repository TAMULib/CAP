package edu.tamu.cap.service;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.response.RVContext;
import edu.tamu.cap.model.response.Triple;

@Service("DSpace")
public class DSpaceService implements RVService<Model> {
    
    @Override
    public RVContext getRVContext(String contextUri) throws Exception {

        return null;
    }
    
    @Override
    public List<Triple> getTriples(RVService<?> rvService, String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RVContext createChild(String contextUri, List<Triple> metadata) throws Exception {
        return null;
    }

    @Override
    public RVContext createResource(String contextUri, MultipartFile file) throws Exception {

        return null;
    }

    @Override
    public RVContext createMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

    @Override
    public RVContext updateMetadata(String contextUri, Triple originalTriple, String query) throws Exception {

        return null;
    }
    
    @Override
    public void setRv(RV rv) {

    }

    @Override
    public RVContext buildRVContext(Model model, String contextUri) {

        return null;
    }

    @Override
    public RVContext deleteMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

    @Override
    public List<Triple> getMetadata(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Triple> getChildren(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RVContext getResource(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteResource(String contextUri) throws Exception{
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteRVContext(String targetUri) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    public RV getRV() {
        return null;
    }

}