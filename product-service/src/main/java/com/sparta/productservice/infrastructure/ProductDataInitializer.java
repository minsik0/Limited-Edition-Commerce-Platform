package com.sparta.productservice.infrastructure;


import com.sparta.productservice.domain.option.ProductOption;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ProductDataInitializer implements CommandLineRunner {

    private final EntityManager em;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    private static final int PRODUCT_COUNT = 100_000;
    private static final int BATCH_SIZE = 1000;

    private static final List<String> SIZES =
            List.of("S", "M", "L", "XL");

    private static final List<String> COLORS =
            List.of("BLACK", "WHITE", "RED", "BLUE", "GREEN");

    @Override
    @Transactional
    public void run(String... args) {

        Faker faker = new Faker(new Locale("ko"));
        Random random = new Random();

        long start = System.currentTimeMillis();

        for (int i = 1; i <= PRODUCT_COUNT; i++) {
            Product product = Product.builder()
                    .name(faker.commerce().productName())
                    .price(random.nextInt(50000) + 1000)
                    .maxPurchasePerUser(random.nextInt(5) + 1)
                    .openAt(LocalDateTime.now()
                            .minusDays(random.nextInt(365)))
                    .status(random.nextBoolean()
                            ? ProductStatus.OPEN
                            : ProductStatus.READY)
                    .build();

            int optionCount = random.nextInt(3) + 3;

            for (int j = 0; j < optionCount; j++) {
                ProductOption option = ProductOption.builder()
                        .product(product)
                        .size(SIZES.get(random.nextInt(SIZES.size())))
                        .color(COLORS.get(random.nextInt(COLORS.size())))
                        .remainStock(random.nextInt(100))
                        .build();

                product.getOptions().add(option);
            }

            em.persist(product);

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("상품 더미 데이터 생성 완료: " + (end - start) + "ms");
    }
}
