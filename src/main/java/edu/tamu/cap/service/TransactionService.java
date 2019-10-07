package edu.tamu.cap.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionService.class);

    private static final Map<String, ZonedDateTime> transactions = new ConcurrentHashMap<String, ZonedDateTime>();

    public void add(String tid, Duration duration) {
        logger.info(String.format("Managing transaction with id %s", tid));
        transactions.put(tid, now().plus(duration));
    }

    public Optional<ZonedDateTime> get(String tid) {
        return Optional.ofNullable(transactions.get(tid));
    }

    public void remove(String tid) {
        logger.info(String.format("Removing transaction with id %s", tid));
        transactions.remove(tid);
    }

    public int count() {
        return transactions.size();
    }

    public void clear() {
        transactions.clear();
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    @Scheduled(fixedRate = 180000)
    void expire() {
        transactions.forEach((tid, t) -> {
            if (t.isBefore(now())) {
                remove(tid);
            }
        });
    }

}