package com.handelsbanken.ecommerce.checkout_service.service;

import com.handelsbanken.ecommerce.checkout_service.domain.entity.WatchCatalogueEntity;
import com.handelsbanken.ecommerce.checkout_service.domain.model.WatchCatalogue;
import com.handelsbanken.ecommerce.checkout_service.dto.CartItem;
import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutRequest;
import com.handelsbanken.ecommerce.checkout_service.dto.CheckoutResponse;
import com.handelsbanken.ecommerce.checkout_service.mapper.WatchCatalogueMapper;
import com.handelsbanken.ecommerce.checkout_service.repository.WatchCatalogueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CheckoutService {

    private final WatchCatalogueRepository watchCatalogueRepository;
    private final WatchCatalogueMapper watchCatalogueMapper;

    public CheckoutService(WatchCatalogueRepository watchCatalogueRepository, WatchCatalogueMapper watchCatalogueMapper) {
        this.watchCatalogueRepository = watchCatalogueRepository;
        this.watchCatalogueMapper = watchCatalogueMapper;
    }

    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        log.info("Processing checkout for userId: {}, cartId: {}", checkoutRequest.userId(), checkoutRequest.cartId());
        BigDecimal totalCost = BigDecimal.ZERO;
        Map<String, Integer> aggregatedQuantityMap = checkoutRequest.items().stream()
                .collect(Collectors.toMap(
                        CartItem::watchId,
                        CartItem::quantity,
                        Integer::sum
                ));
        for (Map.Entry<String, Integer> entry : aggregatedQuantityMap.entrySet()) {
            String watchId = entry.getKey();
            int quantity = entry.getValue();
            log.info("Processing cart item: watchId: {}, quantity: {}", watchId, quantity);
            WatchCatalogueEntity watchCatalogueEntity = watchCatalogueRepository.findById(watchId)
                    .orElseThrow(() -> new IllegalArgumentException("Watch with ID " + watchId + " not found"));
            WatchCatalogue watchCatalogue = watchCatalogueMapper.toDomain(watchCatalogueEntity);
            BigDecimal itemCost = watchCatalogue.discountStrategy().calculatePrice(quantity, watchCatalogue.unitPrice());
            totalCost = totalCost.add(itemCost);
        }

        log.info("Checkout completed for userId: {}, cartId: {}, totalCost: {}", checkoutRequest.userId(), checkoutRequest.cartId(), totalCost);
        String orderId = UUID.randomUUID().toString();
        return new CheckoutResponse(
                checkoutRequest.userId(),
                orderId,
                "COMPLETED",
                totalCost
        );
    }
}
