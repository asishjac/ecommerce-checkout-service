package com.handelsbanken.ecommerce.checkout_service.domain.discount;

import java.math.BigDecimal;

public class BulkDiscountStrategy implements DiscountStrategy {

    private final int requiredQuantity;
    private final BigDecimal discountedPrice;

    public BulkDiscountStrategy(int requiredQuantity, BigDecimal discountedPrice) {
        this.requiredQuantity = requiredQuantity;
        this.discountedPrice = discountedPrice;
    }

    @Override
    public BigDecimal calculatePrice(int quantity, BigDecimal unitPrice) {
        if (quantity < requiredQuantity) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        int setsQualifiedForDiscount = quantity / requiredQuantity;
        int remainingQuantity = quantity % requiredQuantity;

        BigDecimal discountedTotal = discountedPrice.multiply(BigDecimal.valueOf(setsQualifiedForDiscount));
        BigDecimal remainingTotal = unitPrice.multiply(BigDecimal.valueOf(remainingQuantity));

        return discountedTotal.add(remainingTotal);
    }

    @Override
    public String getDescription() {
        return requiredQuantity + " for " + discountedPrice;
    }
}
