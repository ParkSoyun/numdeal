package com.numble.numdeal;

import com.numble.numdeal.layer.domain.Product;
import com.numble.numdeal.layer.repository.ProductRepository;
import com.numble.numdeal.layer.service.OrderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcurrencyControlTest {
    public static int CONCURRENCY_COUNT = 100;
    public static int CONCURRENCY_USER = 1000;

    @Autowired
    private OrderService orderService;

    @Autowired
    private  ProductRepository productRepository;

    @Test
    public void 동시에_1000명이_주문() throws InterruptedException {
        int threadCount = CONCURRENCY_USER;

        // 멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있도록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.reduceStockAndReturnProduct(4L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = productRepository.findById(4L).orElseThrow();

        assertEquals(0, product.getStock());
    }
}
