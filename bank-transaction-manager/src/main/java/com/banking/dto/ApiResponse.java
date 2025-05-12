package com.banking.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * ApiResponse 是一个通用的 API 响应类，用于封装 API 响应数据。
 *
 * @param <T> 响应数据类型
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, Object> errors;

    public ApiResponse(boolean success,  T data, String message) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = new HashMap<>();
    }

    // Getter and Setter methods

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public void addError(String field, Object error) {
        this.errors.put(field, error);
    }
}