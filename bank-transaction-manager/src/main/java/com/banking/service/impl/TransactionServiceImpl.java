package com.banking.service.impl;

import com.banking.exception.InvalidInputException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * TransactionServiceImpl 是 TransactionService 接口的具体实现类。
 * 提供了基于内存存储的交易管理功能，支持创建、查询、更新和删除操作。
 *
 * <p>该实现使用 ConcurrentHashMap 来保证线程安全，并通过自增 ID 实现交易记录的唯一标识。</p>
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    /**
     * 存储所有交易记录的内存数据库，键为交易ID，值为交易对象。
     */
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    /**
     * 当前用于生成新交易ID的自增计数器。
     */
    private Long currentId = 0L;

    /**
     * 创建一个新的交易记录。
     *
     * @param transaction 要创建的交易对象
     * @return 返回已创建的交易对象，包含系统分配的唯一ID
     * @throws InvalidInputException 如果传入的交易对象不合法
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        validateTransaction(transaction);

        // 分配新ID并保存到内存中
        transaction.setId(++currentId);
        transactions.put(currentId, transaction);
        return transaction;
    }

    /**
     * 获取分页形式的交易列表。
     *
     * @param page 分页页码（从0开始）
     * @param size 每页记录数量
     * @return 返回当前页的交易记录列表（按ID升序排列）
     * @throws InvalidInputException 如果分页参数非法（如负数或无效大小）
     */
    @Override
    public List<Transaction> getAllTransactions(int page, int size) {
        if (page < 0 || size < 1) {
            throw new InvalidInputException("Page number must be >= 0 and size must be >= 1");
        }

        // 将交易集合转换为按ID排序的列表
        List<Transaction> sortedList = transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getId))
                .collect(Collectors.toList());

        int totalItems = sortedList.size();
        int fromIndex = Math.min(page * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);

        // 返回指定范围内的子列表
        return sortedList.subList(fromIndex, toIndex);
    }

    /**
     * 根据交易ID获取对应的交易记录。
     *
     * @param id 要查询的交易ID
     * @return 返回匹配的交易对象
     * @throws ResourceNotFoundException 如果未找到对应ID的交易记录
     * @throws InvalidInputException 如果提供的ID非法（null 或小于等于0）
     */
    @Override
    public Transaction getTransactionById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("Transaction ID must be positive.");
        }
        Transaction transaction = transactions.get(id);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        return transaction;
    }

    /**
     * 删除指定ID的交易记录。
     *
     * @param id 要删除的交易ID
     * @throws InvalidInputException 如果提供的ID非法（null 或小于等于0）
     * @throws ResourceNotFoundException 如果未找到对应ID的交易记录
     */
    @Override
    public void deleteTransaction(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("Transaction ID must be positive.");
        }
        if (!transactions.containsKey(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        transactions.remove(id);
    }

    /**
     * 更新指定ID的交易记录。
     *
     * @param id 要更新的交易ID
     * @param transactionDetails 包含新数据的交易对象
     * @return 返回更新后的交易对象
     * @throws InvalidInputException 如果提供的ID或新数据不合法
     * @throws ResourceNotFoundException 如果未找到对应ID的交易记录
     */
    @Override
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("Transaction ID must be positive.");
        }
        validateTransaction(transactionDetails);

        if (!transactions.containsKey(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }

        // 获取现有记录并更新字段
        Transaction existing = transactions.get(id);
        existing.setType(transactionDetails.getType());
        existing.setAmount(transactionDetails.getAmount());

        transactions.put(id, existing);
        return existing;
    }

    /**
     * 对交易对象进行合法性校验。
     *
     * @param transaction 待校验的交易对象
     * @throws InvalidInputException 如果校验失败，抛出包含错误信息的异常
     */
    private void validateTransaction(Transaction transaction) {
        List<String> errors = new ArrayList<>();

        if (transaction == null) {
            throw new InvalidInputException("Transaction cannot be null.");
        }

        if (transaction.getType() == null || transaction.getType().trim().isEmpty()) {
            errors.add("Transaction type cannot be empty.");
        }

        if (transaction.getAmount() <= 0) {
            errors.add("Transaction amount must be greater than zero.");
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(String.join(" | ", errors));
        }
    }
}