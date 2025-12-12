package com.handelsbanken.ecommerce.checkout_service.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;

import java.math.BigDecimal;

public record WatchCatalogue(
        String id,
        String name,
        BigDecimal unitPrice,
        @JsonIgnore
        DiscountStrategy discountStrategy
) {
    @JsonProperty("discount")
    public String getDiscountDescription() {
        return discountStrategy.getDescription();
    }
}
