package edu.tamu.cap.model.ircontext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FedoraTransactionDetails implements TransactionDetails {
    
    private String transactionToken;
    private ZonedDateTime expirationDate;
       
    public FedoraTransactionDetails() {}
    
    public FedoraTransactionDetails(String transactionToken) {
        super();
        this.transactionToken = transactionToken;
    }
    
    public FedoraTransactionDetails(String transactionToken, String expirationString) {
        this(transactionToken);
        setExpirationDate(expirationString);
    }

    public String getTransactionToken() {
        return transactionToken;
    }
    
    public void setExpirationDate(String expirationString) {
        expirationDate = ZonedDateTime.parse(expirationString, DateTimeFormatter.ISO_ZONED_DATE_TIME);        
    }
    
    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }
    
    public String getExpirationDateString() {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(getExpirationDate());
    }

    public Boolean isActive() {
        return getSecondsRemaining() < 1;        
    }

    public int getSecondsRemaining() {
        return (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")), getExpirationDate());        
    }
}
