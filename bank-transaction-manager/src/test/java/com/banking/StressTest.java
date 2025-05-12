package com.banking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StressTest {

    @Autowired
    private MockMvc mockMvc;

    // 测试参数
    private static final int THREAD_COUNT = 1000;         // 并发线程数
    private static final int REQUESTS_PER_THREAD = 100;    // 每个线程发送多少次请求
    private static final String TRANSACTION_JSON = "{\"type\":\"Deposit\",\"amount\":100.0}";

    @Test
    public void stressTestCreateTransaction() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        BlockingQueue<Long> responseTimes = new LinkedBlockingQueue<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    try {
                        long start = System.currentTimeMillis();
                        MvcResult result = mockMvc.perform(
                                        MockMvcRequestBuilders.post("/api/transactions")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TRANSACTION_JSON))
                                .andExpect(status().isCreated())
                                .andReturn();

                        long duration = System.currentTimeMillis() - start;
                        responseTimes.add(duration);
                        successCount.incrementAndGet();

                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    }
                }
                latch.countDown();
            });
        }

        latch.await(); // 等待所有线程完成
        executor.shutdown();

        long totalTime = System.currentTimeMillis() - startTime;
        int totalRequests = THREAD_COUNT * REQUESTS_PER_THREAD;
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        double requestsPerSecond = (double) totalRequests / (totalTime / 1000.0);

        // 输出压测报告
        System.out.println("====== 压力测试结果 ======");
        System.out.println("总请求数: " + totalRequests);
        System.out.println("成功请求数: " + successCount.get());
        System.out.println("失败请求数: " + failureCount.get());
        System.out.printf("平均响应时间: %.2f ms%n", avgResponseTime);
        System.out.println("最大响应时间: " + maxResponseTime + " ms");
        System.out.printf("吞吐量（RPS）: %.2f 请求/秒%n", requestsPerSecond);
        System.out.println("=========================");
    }
}