package com.handelsbanken.ecommerce.checkout_service.discount;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoDiscountStrategyTest {

    @Test
    void calculatePrice_NoDiscountApplied() {
        DiscountStrategy strategy = new NoDiscountStrategy();
        BigDecimal price = strategy.calculatePrice(3, BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(300), price);
    }
}
