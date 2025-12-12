package com.handelsbanken.ecommerce.checkout_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkout_ShouldReturnCorrectTotal_ForMixedCart() throws Exception {
        // Updated JSON structure
        String requestJson = """
            {
              "userId": "user-123",
              "cartId": "cart-456",
              "items": [
                { "watchId": "001", "quantity": 2 },
                { "watchId": "002", "quantity": 1 },
                { "watchId": "003", "quantity": 1 },
                { "watchId": "004", "quantity": 1 }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.orderStatus").value("COMPLETED"))
                // Calculation:
                // 2 Rolex (001) = 200 (No discount req 3) -> Wait, logic: 2*100 = 200
                // 1 MK (002) = 80
                // 1 Swatch (003) = 50
                // 1 Casio (004) = 30
                // Total = 200 + 80 + 50 + 30 = 360
                // Total = 200 + 80 + 50 + 30 = 360
                .andExpect(jsonPath("$.totalCost").value(360.00));
    }

    @Test
    void checkout_ShouldApplyBulkDiscount_Rolex3For200() throws Exception {
        String requestJson = """
            {
              "userId": "user-123",
              "cartId": "cart-456",
              "items": [
                { "watchId": "001", "quantity": 3 }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(200.00));
    }

    @Test
    void checkout_ShouldReturn404_WhenWatchNotFound() throws Exception {
        String requestJson = """
            {
              "userId": "user-123",
              "cartId": "cart-456",
              "items": [
                { "watchId": "001", "quantity": 1 },
                { "watchId": "INVALID_ID", "quantity": 1 }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Watch with ID INVALID_ID not found"));
    }
}
