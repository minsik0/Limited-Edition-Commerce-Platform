package com.sparta.productservice;

import com.sparta.productservice.domain.option.ProductOption;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class StockConcurrencyTest {

    @Autowired ProductOptionRepository productOptionRepository;
    @Autowired ProductRepository productRepository;

    @Test
    void 동시_150명_주문시_재고_100개_정확히_차감() throws InterruptedException {
        // given
        Product product = productRepository.save(
                Product.builder()
                        .name("테스트 한정판")
                        .price(10000)
                        .maxPurchasePerUser(5)
                        .openAt(LocalDateTime.now().minusDays(1))
                        .status(ProductStatus.OPEN)
                        .build()
        );

        ProductOption option = productOptionRepository.save(
                ProductOption.builder()
                        .product(product)
                        .size("M")
                        .color("BLACK")
                        .remainStock(100)
                        .build()
        );

        UUID productId = product.getProductId();
        UUID optionId = option.getOptionId();

        int threadCount = 150;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    int updated = productOptionRepository.decreaseStockAtomic(optionId, productId, 1);
                    if (updated > 0) successCount.incrementAndGet();
                    else failCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("스레드 예외: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(successCount.get()).isEqualTo(100);
        assertThat(failCount.get()).isEqualTo(50);

        option = productOptionRepository.findById(optionId).get();
        assertThat(option.getRemainStock()).isEqualTo(0);
    }
}
