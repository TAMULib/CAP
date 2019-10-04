package edu.tamu.cap.model.response;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TransactionDetails {

    private final String token;

    private final ZonedDateTime expiration;

    public TransactionDetails(String token, ZonedDateTime expiration) {
        super();
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getExpiration() {
        return expiration;
    }

    public int getSecondsRemaining() {
        return (int) ChronoUnit.SECONDS.between(ZonedDateTime.now(ZoneId.of("GMT")), expiration);
    }

    public static TransactionDetails of(String token, String expiration) {
        return new TransactionDetails(token, ZonedDateTime.parse(expiration, DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    public static TransactionDetails of(String token, ZonedDateTime expiration) {
        return new TransactionDetails(token, expiration);
    }

}
