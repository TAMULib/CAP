package edu.tamu.cap.model.rvcontext;

import java.time.ZonedDateTime;

public interface TransactionDetails {

       public String getTransactionToken();
       public ZonedDateTime getExpirationDate();
       public int getSecondsRemaining();
       public String getExpirationDateString();
    
}

