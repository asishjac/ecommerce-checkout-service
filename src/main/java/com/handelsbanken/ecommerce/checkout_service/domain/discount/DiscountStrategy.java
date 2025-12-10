package com.handelsbanken.ecommerce.checkout_service.domain.discount;

public interface DiscountStrategy {
    int calculatePrice(int quantity, int unitPrice);
}
