package com.banking.service;


import com.banking.model.Transaction;
import java.util.List;


public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions(int page, int size);
    Transaction getTransactionById(Long id);
    void deleteTransaction(Long id);
    Transaction updateTransaction(Long id, Transaction transactionDetails);
}