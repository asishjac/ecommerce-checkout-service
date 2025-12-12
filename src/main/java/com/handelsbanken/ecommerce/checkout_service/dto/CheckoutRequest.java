package com.handelsbanken.ecommerce.checkout_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutRequest(
        @NotBlank(message = "User ID is required")
        String userId,

        @NotBlank(message = "Cart ID is required")
        String cartId,

        @NotEmpty(message = "Cart items list cannot be empty")
        @Valid
        List<CartItem> items
) {}
