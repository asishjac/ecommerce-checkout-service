package com.handelsbanken.ecommerce.checkout_service.discount;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulkDiscountStrategyTest {

    @Test
    void calculatePrice_ShouldApplyDiscount_WhenRequiredQuantityProvided() {
        // 3 for 200
        DiscountStrategy strategy = new BulkDiscountStrategy(3, 200);
        int price = strategy.calculatePrice(3, 100);
        assertEquals(200, price);
    }

    @Test
    void calculatePrice_ShouldNotApplyDiscount_WhenNoRequiredQuantityProvided() {
        DiscountStrategy strategy = new BulkDiscountStrategy(3, 200);
        int price = strategy.calculatePrice(1, 100);
        assertEquals(100, price);
    }
}
