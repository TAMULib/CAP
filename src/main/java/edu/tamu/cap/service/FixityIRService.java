package edu.tamu.cap.service;

import edu.tamu.cap.model.response.IRContext;

public interface FixityIRService<M> extends IRService<M> {
    public IRContext resourceFixity(String contextUri) throws Exception;
}
