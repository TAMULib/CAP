package edu.tamu.cap.service;

import java.util.List;

import edu.tamu.cap.model.response.IRContext;
import edu.tamu.cap.model.response.Version;

public interface Versioning<M> extends IRService<M> {
    
    public IRContext createVersion(String contextUri, String Name) throws Exception;

    public List<Version> getVersions(String contextUri) throws Exception;

    public IRContext restoreVersion(String contextUri) throws Exception;

    public void deleteVersion(String contextUri) throws Exception;

}
