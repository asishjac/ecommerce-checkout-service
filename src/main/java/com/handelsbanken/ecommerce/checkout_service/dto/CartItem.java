package com.handelsbanken.ecommerce.checkout_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CartItem(
        @NotBlank(message = "Watch ID is required")
        String watchId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {}
