package com.handelsbanken.ecommerce.checkout_service.discount;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoDiscountStrategyTest {

    @Test
    void calculatePrice_NoDiscountApplied() {
        DiscountStrategy strategy = new NoDiscountStrategy();
        int price = strategy.calculatePrice(3, 100);
        assertEquals(300, price);
    }
}
