package edu.tamu.cap.model.ircontext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FedoraTransactionDetails implements TransactionDetails {
    
    private String transactionToken;
    private ZonedDateTime expirationDate;
    
    
    private static final String TIME_FORMAT_PATTERN = "EEE, dd MMM uuuu kk:mm:ss z";
    
    public FedoraTransactionDetails() {}
    
    public FedoraTransactionDetails(String transactionToken) {
        super();
        this.transactionToken = transactionToken;
    }
    
    public FedoraTransactionDetails(String transactionToken, String expirationString) {
        this(transactionToken);
        setExpirationDate(expirationString);
    }
    
    public FedoraTransactionDetails(String transactionToken, int maxAge) { 
        this(transactionToken);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        setExpirationDate(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN).format(now).toString());
    }

    public String getTransactionToken() {
        return transactionToken;
    }
    
    public void setExpirationDate(String expirationString) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN);
        expirationDate = ZonedDateTime.parse(expirationString,f);        
    }
    
    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public Boolean isActive() {
        return getSecondsRemaining() < 1;        
    }

    public int getSecondsRemaining() {
        return (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")), getExpirationDate());        
    }
}
