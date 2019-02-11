package edu.tamu.cap.service;

import edu.tamu.cap.model.response.RVContext;

public interface QueryableRVService<M> extends RVService<M> {

    public RVContext query(String contextUri, String query) throws Exception;
    
    public String getQueryHelp() throws Exception;
    
}
