package com.banking;

import com.banking.controller.TransactionController;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        testTransaction = new Transaction(1L, "Deposit", 100.0);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(testTransaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"Deposit\",\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.type", is("Deposit")))
                .andExpect(jsonPath("$.data.amount", is(100.0)));

        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }


    @Test
    public void testGetAllTransactions_EmptyPage_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("page", "999")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(eq(1L));

        mockMvc.perform(delete("/api/transactions/{id}", 1L))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).deleteTransaction(eq(1L));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Transaction updatedTransaction = new Transaction(1L, "Withdrawal", 50.0);
        when(transactionService.updateTransaction(eq(1L), any(Transaction.class))).thenReturn(updatedTransaction);

        mockMvc.perform(put("/api/transactions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"Withdrawal\",\"amount\":50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.type", is("Withdrawal"))) // 更新后的类型应为 Withdrawal
                .andExpect(jsonPath("$.data.amount", is(50.0))); // 更新后的金额应为 50.0

        verify(transactionService, times(1)).updateTransaction(eq(1L), any(Transaction.class));
    }
}