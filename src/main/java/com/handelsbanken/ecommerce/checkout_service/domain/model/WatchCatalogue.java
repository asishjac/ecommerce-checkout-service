package com.handelsbanken.ecommerce.checkout_service.domain.model;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;

import java.math.BigDecimal;

public record WatchCatalogue(
        String id,
        String name,
        BigDecimal unitPrice,
        DiscountStrategy discountStrategy
) {}
