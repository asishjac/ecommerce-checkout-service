package com.handelsbanken.ecommerce.checkout_service.service;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.entity.WatchCatalogueEntity;
import com.handelsbanken.ecommerce.checkout_service.domain.model.WatchCatalogue;
import com.handelsbanken.ecommerce.checkout_service.dto.CartItem;
import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutRequest;
import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutResponse;
import com.handelsbanken.ecommerce.checkout_service.mapper.WatchCatalogueMapper;
import com.handelsbanken.ecommerce.checkout_service.repository.WatchCatalogueRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class CheckoutServiceTest {

    private final WatchCatalogueRepository watchCatalogueRepositoryMock = Mockito.mock(WatchCatalogueRepository.class);
    private final WatchCatalogueMapper watchCatalogueMapperMock = Mockito.mock(WatchCatalogueMapper.class);

    private final CheckoutService checkoutService = new CheckoutService(watchCatalogueRepositoryMock, watchCatalogueMapperMock);

    @Test
    void checkout_SingleItem_ReturnsCalculatedTotal() {

        CartItem item = new CartItem("004", 1);
        CheckoutRequest request = new CheckoutRequest("user-1", "cart-1", List.of(item));

        WatchCatalogueEntity entity = new WatchCatalogueEntity(
                "004",
                "Swatch",
                BigDecimal.valueOf(1000),
                null
        );
        when(watchCatalogueRepositoryMock.findById("004")).thenReturn(Optional.of(entity));

        WatchCatalogue watchCatalogue = new WatchCatalogue(
                "004",
                "Swatch",
                BigDecimal.valueOf(1000),
                new NoDiscountStrategy()
        );
        when(watchCatalogueMapperMock.toDomain(entity)).thenReturn(watchCatalogue);

        CheckoutResponse response = checkoutService.checkout(request);

        // Assert
        assertEquals("user-1", response.userId());
        assertEquals("COMPLETED", response.orderStatus());
        assertEquals(BigDecimal.valueOf(1000), response.totalCost());
        assertNotNull(response.orderId());

        verify(watchCatalogueRepositoryMock, times(1)).findById("004");
        verify(watchCatalogueMapperMock, times(1)).toDomain(entity);
    }

    @Test
    void checkout_MultipleItems_ReturnsAccumulatedTotal() {

        CartItem item1 = new CartItem("001", 3);
        CartItem item2 = new CartItem("002", 5);
        CheckoutRequest request = new CheckoutRequest("user-2", "cart-2", List.of(item1, item2));

        WatchCatalogueEntity e1 = new WatchCatalogueEntity(
                "001",
                "Rolex",
                BigDecimal.valueOf(100),
                "2 for 150"
        );
        WatchCatalogueEntity e2 = new WatchCatalogueEntity(
                "002",
                "Casio",
                BigDecimal.valueOf(50),
                null
        );
        when(watchCatalogueRepositoryMock.findById("001")).thenReturn(Optional.of(e1));
        when(watchCatalogueRepositoryMock.findById("002")).thenReturn(Optional.of(e2));

        WatchCatalogue w1 = new WatchCatalogue(
                "001",
                "Rolex",
                BigDecimal.valueOf(100),
                new BulkDiscountStrategy(2,BigDecimal.valueOf(150))
        );
        WatchCatalogue w2 = new WatchCatalogue(
                "002",
                "Casio",
                BigDecimal.valueOf(50),
                new NoDiscountStrategy()
        );
        when(watchCatalogueMapperMock.toDomain(e1)).thenReturn(w1);
        when(watchCatalogueMapperMock.toDomain(e2)).thenReturn(w2);

        CheckoutResponse response = checkoutService.checkout(request);

        assertEquals("user-2", response.userId());
        assertEquals("COMPLETED", response.orderStatus());
        assertEquals(BigDecimal.valueOf(500), response.totalCost());

        verify(watchCatalogueRepositoryMock, times(1)).findById("001");
        verify(watchCatalogueRepositoryMock, times(1)).findById("002");
        verify(watchCatalogueMapperMock, times(1)).toDomain(e1);
        verify(watchCatalogueMapperMock, times(1)).toDomain(e2);
    }

    @Test
    void checkout_MultipleItems_WithSameID_ReturnsCalculatedTotal() {

        CartItem item1 = new CartItem("004", 1);
        CartItem item2 = new CartItem("004", 1);
        CheckoutRequest request = new CheckoutRequest("user-1", "cart-1", List.of(item1, item2));

        WatchCatalogueEntity entity = new WatchCatalogueEntity(
                "004",
                "Swatch",
                BigDecimal.valueOf(1000),
                "2 for 1000"
        );
        when(watchCatalogueRepositoryMock.findById("004")).thenReturn(Optional.of(entity));

        WatchCatalogue watchCatalogue = new WatchCatalogue(
                "004",
                "Swatch",
                BigDecimal.valueOf(1000),
                new BulkDiscountStrategy(2,BigDecimal.valueOf(1000))
        );
        when(watchCatalogueMapperMock.toDomain(entity)).thenReturn(watchCatalogue);

        CheckoutResponse response = checkoutService.checkout(request);

        // Assert
        assertEquals("user-1", response.userId());
        assertEquals("COMPLETED", response.orderStatus());
        assertEquals(BigDecimal.valueOf(1000), response.totalCost());
        assertNotNull(response.orderId());

        verify(watchCatalogueRepositoryMock, times(1)).findById("004");
        verify(watchCatalogueMapperMock, times(1)).toDomain(entity);
    }

    @Test
    void checkout_WatchIdNotFound_ThrowsIllegalArgumentException() {
        CartItem item = new CartItem("001", 1);
        CheckoutRequest request = new CheckoutRequest("user-3", "cart-3", List.of(item));

        when(watchCatalogueRepositoryMock.findById("001")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> checkoutService.checkout(request));
        assertTrue(ex.getMessage().contains("Watch with ID 001 not found"));

        verify(watchCatalogueRepositoryMock, times(1)).findById("001");
        verifyNoInteractions(watchCatalogueMapperMock);
    }

    @Test
    void checkout_SingleItem_IntegerMaxQuantity_OverflowsIntTotal() {

        CartItem item = new CartItem("004", Integer.MAX_VALUE);
        CheckoutRequest request = new CheckoutRequest("user-1", "cart-1", List.of(item));

        WatchCatalogueEntity entity = new WatchCatalogueEntity(
                "004",
                "Swatch",
                BigDecimal.valueOf(100),
                null
        );
        when(watchCatalogueRepositoryMock.findById("004")).thenReturn(Optional.of(entity));

        WatchCatalogue watchCatalogue = new WatchCatalogue(
                "004",
                "Swatch",
                BigDecimal.valueOf(100),
                new NoDiscountStrategy()
        );
        when(watchCatalogueMapperMock.toDomain(entity)).thenReturn(watchCatalogue);

        CheckoutResponse response = checkoutService.checkout(request);

        // Assert
        assertEquals("user-1", response.userId());
        assertEquals("COMPLETED", response.orderStatus());
        assertEquals(BigDecimal.valueOf(100L * Integer.MAX_VALUE), response.totalCost());
        assertNotNull(response.orderId());

        verify(watchCatalogueRepositoryMock, times(1)).findById("004");
        verify(watchCatalogueMapperMock, times(1)).toDomain(entity);
    }

}
