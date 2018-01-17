package edu.tamu.cap.service;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;

@Service("DSpace")
public class DSpaceService implements IRService<Model> {

    @Override
    public void verifyPing() throws Exception {

    }

    @Override
    public void verifyAuth() throws Exception {

    }

    @Override
    public void verifyRoot() throws Exception {

    }

    @Override
    public IRContext createContainer(String contextUri, String name) throws Exception {

        return null;
    }

    @Override
    public IRContext createResource(String contextUri, MultipartFile file) throws Exception {

        return null;
    }

    @Override
    public IRContext getContainer(String contextUri) throws Exception {

        return null;
    }

    @Override
    public IRContext createMetadata(Triple triple) throws Exception {

        return null;
    }

    @Override
    public IRContext updateMetadata(String contextUri, String sparql) throws Exception {

        return null;
    }

    @Override
    public void deleteContainer(String targetUri) throws Exception {

    }

    @Override
    public void setIr(IR ir) {

    }

    @Override
    public IRContext buildIRContext(Model model, String contextUri) {

        return null;
    }

    @Override
    public IRContext deleteMetadata(Triple triple) throws Exception {

        return null;
    }

	@Override
	public IRContext resourceFixity(Triple tiple) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}