package edu.tamu.cap.service;

import java.util.List;

import edu.tamu.cap.model.response.RVContext;
import edu.tamu.cap.model.response.Version;

public interface VersioningRVService<M> extends RVService<M> {
    
    public RVContext createVersion(String contextUri, String Name) throws Exception;

    public List<Version> getVersions(String contextUri) throws Exception;

    public RVContext restoreVersion(String contextUri) throws Exception;

    public void deleteVersion(String contextUri) throws Exception;

}
