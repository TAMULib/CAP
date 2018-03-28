package edu.tamu.cap.service;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

@Service("DSpace")
public class DSpaceService implements IRService<Model> {
    
    @Override
    public IRContext getIRContext(String contextUri) throws Exception {

        return null;
    }
    
    @Override
    public List<Triple> getTriples(IRService<?> irService, String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRContext createChild(String contextUri, List<Triple> metadata) throws Exception {
        return null;
    }

    @Override
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception {

        return null;
    }

    @Override
    public IRContext createMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

    @Override
    public IRContext updateMetadata(String contextUri, Triple originalTriple, String query) throws Exception {

        return null;
    }
    
    @Override
    public void setIr(IR ir) {

    }

    @Override
    public IRContext buildIRContext(Model model, String contextUri) {

        return null;
    }

    @Override
    public IRContext deleteMetadata(String contextUri, Triple triple) throws Exception {

        return null;
    }

	@Override
	public IRContext resourceFixity(String contextUri) throws Exception {
		// TODO Auto-generated method stub
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
    public IRContext getResource(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteResource(String contextUri) throws Exception{
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteIRContext(String targetUri) throws Exception {
        // TODO Auto-generated method stub
        
    }

}