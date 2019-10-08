package edu.tamu.cap.service.repositoryview;

import edu.tamu.cap.model.response.TransactionDetails;

public interface TransactingRepositoryViewService<M> extends RepositoryViewService<M> {

    public TransactionDetails startTransaction() throws Exception;

    public TransactionDetails refreshTransaction(String tokenURI) throws Exception;

    public TransactionDetails makeTransactionDetails(String transactionToken, String expirationString) throws Exception;

    public void commitTransaction(String tokenURI) throws Exception;

    public void rollbackTransaction(String tokenURI) throws Exception;

}
