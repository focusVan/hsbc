package com.banking.controller;

import com.banking.dto.ApiResponse;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransactionController 是一个 REST 控制器，用于处理与交易(Transaction)相关的 HTTP 请求。
 * 提供了创建、查询、更新和删除交易的基本 CRUD 操作。
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * 创建一个新的交易记录。
     *
     * @param transaction 包含交易信息的请求体对象
     * @return ApiResponse<Transaction> 返回创建成功的交易数据，HTTP 状态码 201 Created
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        Transaction created = transactionService.createTransaction(transaction);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, created, "Transaction created successfully"));
    }

    /**
     * 分页获取所有交易记录。
     *
     * @param page 页码（从0开始），默认为0
     * @param size 每页记录数，默认为10
     * @return ApiResponse<List<Transaction>> 返回当前页的交易列表，HTTP 状态码 200 OK
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, transactions, "Transactions fetched successfully"));
    }

    /**
     * 根据交易ID获取单个交易记录。
     *
     * @param id 要查询的交易ID
     * @return ApiResponse<Transaction> 返回匹配的交易对象，HTTP 状态码 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, transaction, "Transaction fetched successfully"));
    }

    /**
     * 根据交易ID更新已有交易记录。
     *
     * @param id 要更新的交易ID
     * @param transactionDetails 包含新数据的交易对象
     * @return ApiResponse<Transaction> 返回更新后的交易对象，HTTP 状态码 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction transactionDetails) {
        Transaction updated = transactionService.updateTransaction(id, transactionDetails);
        return ResponseEntity.ok(new ApiResponse<>(true, updated, "Transaction updated successfully"));
    }

    /**
     * 根据交易ID删除指定的交易记录。
     *
     * @param id 要删除的交易ID
     * @return ResponseEntity<Void> 空响应体，HTTP 状态码 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}