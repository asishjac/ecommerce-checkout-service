package com.handelsbanken.ecommerce.checkout_service.domain.discount;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculatePrice(int quantity, BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
