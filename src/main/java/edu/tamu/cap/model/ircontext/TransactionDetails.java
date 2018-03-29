package edu.tamu.cap.model.ircontext;

import java.time.ZonedDateTime;

public interface TransactionDetails {

       public String getTransactionToken();
       public ZonedDateTime getExpirationDate();
       public int getSecondsRemaining();
       public Boolean isActive();
    
}

