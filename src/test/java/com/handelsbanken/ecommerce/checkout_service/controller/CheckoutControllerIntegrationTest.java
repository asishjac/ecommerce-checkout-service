package com.handelsbanken.ecommerce.checkout_service.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void checkout_ShouldReturn400_WhenJsonIsMalformed() throws Exception {

        String malformedJson = """
                {
                  "userId": "u1",
                  "products": [
                    { "watchId": "001", "quantity": 1 }
                  ]
                """;

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequests")
    void checkout_ShouldReturn400_WhenRequestIsInvalid(String json, String expectedErrorField) throws Exception {
        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details['" + expectedErrorField + "']").exists());
    }

    private static Stream<Arguments> provideInvalidRequests() {
        return Stream.of(
                // Invalid userId
                Arguments.of(
                        """
                                { "userId": "", "cartId": "c1", "items": [{ "watchId": "001", "quantity": 1 }] }
                                """,
                        "userId"
                ),
                // Invalid cartId
                Arguments.of(
                        """
                                { "userId": "u1", "cartId": "", "items": [{ "watchId": "001", "quantity": 1 }] }
                                """,
                        "cartId"
                ),
                // Empty items list
                Arguments.of(
                        """
                                { "userId": "u1", "cartId": "c1", "items": [] }
                                """,
                        "items"
                ),
                // Invalid watchId
                Arguments.of(
                        """
                                { "userId": "u1", "cartId": "c1", "items": [{ "watchId": "", "quantity": 1 }] }
                                """,
                        "items[0].watchId"
                ),
                // Invalid quantity
                Arguments.of(
                        """
                                { "userId": "u1", "cartId": "c1", "items": [{ "watchId": "001", "quantity": 0 }] }
                                """,
                        "items[0].quantity"
                )
        );
    }

    @Test
    void swaggerUi_ShouldBeAvailable() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void apiDocs_ShouldBeAvailable() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
