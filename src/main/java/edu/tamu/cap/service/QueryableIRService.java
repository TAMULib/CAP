package edu.tamu.cap.service;

import edu.tamu.cap.model.response.IRContext;

public interface QueryableIRService<M> extends IRService<M> {

    public IRContext query(String contextUri, String query) throws Exception;
    
    public String getQueryHelp() throws Exception;
    
}
