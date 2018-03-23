package edu.tamu.cap.service;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Triple;
import edu.tamu.cap.model.response.Version;

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
    public IRContext updateMetadata(Triple originalTriple, String sparql) throws Exception {

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

    @Override
    public List<Version> getVersions(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRContext createVersion(String contextUri, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRContext restoreVersion(String contextUri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteVersion(String contextUri) {
    }

    @Override
    public List<Triple> getMetadata(String contextUri) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}