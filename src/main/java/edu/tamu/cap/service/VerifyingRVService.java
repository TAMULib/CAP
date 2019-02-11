package edu.tamu.cap.service;

public interface VerifyingRVService<M> extends RVService<M> {
    
    public void verifyPing() throws Exception;

    public void verifyAuth() throws Exception;

    public void verifyRoot() throws Exception;
    
}
