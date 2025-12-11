package com.handelsbanken.ecommerce.checkout_service.domain.discount;

public interface DiscountStrategy {
    int calculatePrice(int quantity, int unitPrice);

    /**
     * Returns a human-readable description of the discount.
     *
     * @return Description string (e.g., "3 for 200")
     */
    default String getDescription() {
        return "Standard Price";
    }
}
