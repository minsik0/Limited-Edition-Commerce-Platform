package com.sparta.productservice;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.productservice.domain.option.ProductOption;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import com.sparta.productservice.infrastructure.kafka.StockProcessService;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class IdempotencyTest {

    @Autowired StockProcessService stockProcessService;
    @Autowired ProductOptionRepository productOptionRepository;
    @Autowired jakarta.persistence.EntityManager em;

    @Test
    void 동일_이벤트_중복_처리시_재고_한번만_차감() {
        // given: 재고 10개 옵션

        Product product = Product.builder()
                .name("테스트 상품")
                .price(10000)
                .maxPurchasePerUser(5)
                .openAt(LocalDateTime.now().minusDays(1))
                .status(ProductStatus.OPEN)
                .build();
        em.persist(product);

        ProductOption option = ProductOption.builder()
                .product(product)
                .size("M")
                .color("Black")
                .remainStock(10)
                .build();
        em.persist(option);
        UUID eventId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        StockDecreaseEvent event = new StockDecreaseEvent(eventId, orderId, product.getProductId(), option.getOptionId(), 1);

        // when: 같은 이벤트 2번 처리
        stockProcessService.process(event);
        stockProcessService.process(event);

        // then: 재고 9개 (1번만 차감)
        ProductOption savedOption = productOptionRepository.findById(option.getOptionId()).get();
        assertThat(savedOption.getRemainStock()).isEqualTo(9);
    }
}
