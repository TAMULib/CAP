package edu.tamu.cap.service;

import edu.tamu.cap.model.response.FixityReport;

public interface FixityIRService<M> extends IRService<M> {
    public FixityReport resourceFixity(String contextUri) throws Exception;
}
