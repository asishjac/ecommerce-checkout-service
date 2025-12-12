package com.handelsbanken.ecommerce.checkout_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CheckoutResponse(

        @NotBlank(message = "User ID is required")
        String userId,

        @NotBlank(message = "Order ID is required")
        String orderId,

        @NotBlank(message = "Order status is required")
        String orderStatus,

        @Min(0)
        BigDecimal totalCost
) {}