package com.handelsbanken.ecommerce.checkout_service.domain.discount;

public class BulkDiscountStrategy implements DiscountStrategy{

    private final int requiredQuantity;
    private final int discountedPrice;

    public BulkDiscountStrategy(int requiredQuantity, int discountedPrice) {
        this.requiredQuantity = requiredQuantity;
        this.discountedPrice = discountedPrice;
    }
    @Override
    public int calculatePrice(int quantity, int unitPrice) {
        return 0;
    }
}
