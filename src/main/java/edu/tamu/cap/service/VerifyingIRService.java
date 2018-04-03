package edu.tamu.cap.service;

public interface VerifyingIRService<M> extends IRService<M> {
    
    public void verifyPing() throws Exception;

    public void verifyAuth() throws Exception;

    public void verifyRoot() throws Exception;
    
}
