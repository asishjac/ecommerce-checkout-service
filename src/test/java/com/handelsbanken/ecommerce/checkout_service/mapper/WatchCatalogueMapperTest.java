package com.handelsbanken.ecommerce.checkout_service.mapper;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.entity.WatchCatalogueEntity;
import com.handelsbanken.ecommerce.checkout_service.domain.model.WatchCatalogue;
import com.handelsbanken.ecommerce.checkout_service.util.DiscountRuleParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class WatchCatalogueMapperTest {

    private final DiscountRuleParser discountRuleParserMock = Mockito.mock(DiscountRuleParser.class);

    private final WatchCatalogueMapper watchCatalogueMapper = new WatchCatalogueMapper(discountRuleParserMock);

    @Test
    void toDomain_MapsEntityToDomain_WithBulkDiscount() {
        WatchCatalogueEntity entity = new WatchCatalogueEntity();
        entity.setId("001");
        entity.setName("Swatch");
        entity.setUnitPrice(10000);
        entity.setDiscountExpression("3 for 2");

        DiscountStrategy expectedStrategy = new BulkDiscountStrategy(3, 2);
        when(discountRuleParserMock.parse("3 for 2")).thenReturn(expectedStrategy);

        WatchCatalogue domain = watchCatalogueMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getName(), domain.name());
        assertEquals(entity.getUnitPrice(), domain.unitPrice());
        assertSame(expectedStrategy, domain.discountStrategy());

        verify(discountRuleParserMock, times(1)).parse("3 for 2");
    }

    @Test
    void toDomain_MapsEntityToDomain_WithNoDiscount() {
        WatchCatalogueEntity entity = new WatchCatalogueEntity();
        entity.setId("001");
        entity.setName("Swatch");
        entity.setUnitPrice(10000);
        entity.setDiscountExpression("");

        DiscountStrategy expectedStrategy = new NoDiscountStrategy();
        when(discountRuleParserMock.parse("")).thenReturn(expectedStrategy);

        WatchCatalogue domain = watchCatalogueMapper.toDomain(entity);

        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getName(), domain.name());
        assertEquals(entity.getUnitPrice(), domain.unitPrice());
        assertSame(expectedStrategy, domain.discountStrategy());

        verify(discountRuleParserMock, times(1)).parse("");
    }
}
