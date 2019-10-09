package edu.tamu.cap.service.repositoryview;

import edu.tamu.cap.model.response.FixityReport;

public interface FixityRepositoryViewService<M> extends RepositoryViewService<M> {

    public FixityReport resourceFixity(String contextUri) throws Exception;

}
