package edu.tamu.cap.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    private TransactionService transactionService = new TransactionService();

    private String[] tids = { "8e18640e-dadf-11e9-8a34-2a2ae2dbcce4", "8e18679c-dadf-11e9-8a34-2a2ae2dbcce4" };

    @Test
    public void testAdd() {
        transactionService.add(tids[0], Duration.ofHours(1));
        assertEquals(1, transactionService.count());
    }

    @Test
    public void testGet() {
        testAdd();
        assertTrue(transactionService.get(tids[0]).isPresent());
    }

    @Test
    public void testRemove() {
        testAdd();
        transactionService.remove(tids[0]);
        assertEquals(0, transactionService.count());
    }

    @Test
    public void testCount() {
        addAll(Duration.ofDays(1));
        assertEquals(tids.length, transactionService.count());
    }

    @Test
    public void testClear() {
        testCount();
        transactionService.clear();
        assertEquals(0, transactionService.count());
    }

    @Test
    public void testExpire() {
        addAll(Duration.ofSeconds(-1));
        transactionService.expire();
        assertEquals(0, transactionService.count());
    }

    @AfterEach
    public void clear() {
        transactionService.clear();
    }

    private void addAll(Duration duration) {
        for (String tid : tids) {
            transactionService.add(tid, duration);
        }
    }

}