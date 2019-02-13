package edu.tamu.cap.service;

public interface VerifyingRepositoryViewService<M> extends RepositoryViewService<M> {
    
    public void verifyPing() throws Exception;

    public void verifyAuth() throws Exception;

    public void verifyRoot() throws Exception;
    
}
