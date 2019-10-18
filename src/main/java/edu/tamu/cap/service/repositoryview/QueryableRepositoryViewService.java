package edu.tamu.cap.service.repositoryview;

import edu.tamu.cap.model.response.RepositoryViewContext;

public interface QueryableRepositoryViewService<M> extends RepositoryViewService<M> {

    public RepositoryViewContext query(String contextUri, String query) throws Exception;

    public String getQueryHelp() throws Exception;

}
