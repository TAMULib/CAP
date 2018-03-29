package edu.tamu.cap.service;

import edu.tamu.cap.model.ircontext.TransactionDetails;

public interface TransactingIRService<M> extends IRService<M> {
    
    public TransactionDetails startTransaction() throws Exception;
    
    public TransactionDetails makeTransactionDetails(String transactionToken, String expirationString) throws Exception;

    public TransactionDetails makeTransactionDetails(String value, int maxAge) throws Exception;

}
