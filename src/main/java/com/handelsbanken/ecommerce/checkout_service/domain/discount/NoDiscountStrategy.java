package com.handelsbanken.ecommerce.checkout_service.domain.discount;

public class NoDiscountStrategy implements DiscountStrategy{

    @Override
    public int calculatePrice(int quantity, int unitPrice) {
        return 0;
    }
}
