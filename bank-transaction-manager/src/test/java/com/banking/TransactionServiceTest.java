package com.banking;

import com.banking.exception.InvalidInputException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import com.banking.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = new Transaction(null, "Deposit", 100.0);
        Transaction created = transactionService.createTransaction(transaction);
        assertNotNull(created.getId());
        assertEquals("Deposit", created.getType());
        assertEquals(100.0, created.getAmount());
    }

    @Test
    void testCreateTransaction_InvalidInput() {
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            transactionService.createTransaction(new Transaction(null, "", -100.0));
        });
        assertTrue(exception.getMessage().contains("Transaction amount must be greater than zero"));
    }

    @Test
    void testGetAllTransactions_Pagination() {
        // 添加一些数据
        transactionService.createTransaction(new Transaction(null, "Deposit", 100.0));
        transactionService.createTransaction(new Transaction(null, "Withdrawal", 200.0));

        List<Transaction> result = transactionService.getAllTransactions(0, 1);
        assertEquals(1, result.size());
        assertEquals("Deposit", result.get(0).getType());

        result = transactionService.getAllTransactions(1, 1);
        assertEquals(1, result.size());
        assertEquals("Withdrawal", result.get(0).getType());
    }

    @Test
    void testGetTransactionById() {
        Transaction created = transactionService.createTransaction(new Transaction(null, "Deposit", 100.0));
        Transaction retrieved = transactionService.getTransactionById(created.getId());
        assertNotNull(retrieved);
        assertEquals(created, retrieved);
    }

    @Test
    void testGetTransactionById_NotFound() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(999L);
        });
        assertTrue(exception.getMessage().contains("Transaction not found with ID: 999"));
    }

    @Test
    void testDeleteTransaction() {
        Transaction created = transactionService.createTransaction(new Transaction(null, "Deposit", 100.0));
        transactionService.deleteTransaction(created.getId());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(created.getId());
        });
        assertTrue(exception.getMessage().contains("Transaction not found with ID: " + created.getId()));
    }

    @Test
    void testUpdateTransaction() {
        Transaction created = transactionService.createTransaction(new Transaction(null, "Deposit", 100.0));
        Transaction updated = transactionService.updateTransaction(created.getId(), new Transaction(null, "Withdrawal", 200.0));
        assertEquals("Withdrawal", updated.getType());
        assertEquals(200.0, updated.getAmount());
    }

    @Test
    void testUpdateTransaction_NotFound() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.updateTransaction(999L, new Transaction(null, "Deposit", 100.0));
        });
        assertTrue(exception.getMessage().contains("Transaction not found with ID: 999"));
    }
}