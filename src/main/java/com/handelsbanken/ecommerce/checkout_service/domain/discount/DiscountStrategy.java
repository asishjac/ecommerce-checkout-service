package com.handelsbanken.ecommerce.checkout_service.domain.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculatePrice(int quantity, BigDecimal unitPrice);

    /**
     * Returns a human-readable description of the discount.
     *
     * @return Description string (e.g., "3 for 200")
     */
    default String getDescription() {
        return "Standard Price";
    }
}
