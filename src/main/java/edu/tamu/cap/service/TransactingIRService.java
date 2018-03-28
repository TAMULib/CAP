package edu.tamu.cap.service;

public interface TransactingIRService<M> extends IRService<M> {
    
    public String startTransaction() throws Exception;

}
