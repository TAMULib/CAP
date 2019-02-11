package edu.tamu.cap.service;

import edu.tamu.cap.model.response.FixityReport;

public interface FixityRVService<M> extends RVService<M> {
    public FixityReport resourceFixity(String contextUri) throws Exception;
}
