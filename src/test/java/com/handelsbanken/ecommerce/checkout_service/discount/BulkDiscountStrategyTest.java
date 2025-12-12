package com.handelsbanken.ecommerce.checkout_service.discount;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulkDiscountStrategyTest {

    @ParameterizedTest(name = "quantity={0}, unitPrice={1} => expected={2}")
    @CsvSource({
            "3, 100, 200",
            "1, 100, 100",
            "6, 100, 400",
            "4, 100, 300"
    })
    void calculatePrice_ShouldReturnExpectedTotal(int quantity, BigDecimal unitPrice, int expected) {
        // 3 for 200 bulk discount
        DiscountStrategy strategy = new BulkDiscountStrategy(3, BigDecimal.valueOf(200));
        BigDecimal price = strategy.calculatePrice(quantity, unitPrice);
        assertEquals(BigDecimal.valueOf(expected), price);
    }
}