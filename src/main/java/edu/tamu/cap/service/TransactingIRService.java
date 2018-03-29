package edu.tamu.cap.service;

import java.util.Map;

public interface TransactingIRService<M> extends IRService<M> {
    
    public Map<String, String> startTransaction() throws Exception;

}
