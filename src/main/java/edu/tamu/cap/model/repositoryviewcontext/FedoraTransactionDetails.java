package edu.tamu.cap.model.repositoryviewcontext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FedoraTransactionDetails implements TransactionDetails {

    private String transactionToken;

    private ZonedDateTime expirationDate;

    public FedoraTransactionDetails() {
        super();
    }

    public FedoraTransactionDetails(String transactionToken) {
        this();
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

    public int getSecondsRemaining() {
        return (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")), getExpirationDate());
    }

    public int getTransactionDuraction() {
        return (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")), getExpirationDate());
    }

}
