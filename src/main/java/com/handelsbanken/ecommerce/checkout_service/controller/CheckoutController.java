package com.handelsbanken.ecommerce.checkout_service.controller;

import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutRequest;
import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutResponse;
import com.handelsbanken.ecommerce.checkout_service.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        log.info("Received checkout request for user: {}, cart Id: {}", request.userId(), request.cartId());
        CheckoutResponse response = checkoutService.checkout(request);
        return ResponseEntity.ok(response);
    }
}
