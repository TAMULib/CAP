package edu.tamu.cap.service;

import java.util.List;

import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.model.response.Version;

public interface VersioningRepositoryViewService<M> extends RepositoryViewService<M> {

    public RepositoryViewContext createVersion(String contextUri, String versionName) throws Exception;

    public List<Version> getVersions(String contextUri) throws Exception;

    public RepositoryViewContext restoreVersion(String contextUri) throws Exception;

    public void deleteVersion(String contextUri) throws Exception;

}
