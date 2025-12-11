package com.handelsbanken.ecommerce.checkout_service.mapper;

import com.handelsbanken.ecommerce.checkout_service.domain.entity.WatchCatalogueEntity;
import com.handelsbanken.ecommerce.checkout_service.domain.model.WatchCatalogue;
import com.handelsbanken.ecommerce.checkout_service.util.DiscountRuleParser;
import org.springframework.stereotype.Component;

@Component
public class WatchCatalogueMapper {
    private final DiscountRuleParser ruleParser;

    public WatchCatalogueMapper(DiscountRuleParser ruleParser) {
        this.ruleParser = ruleParser;
    }

    public WatchCatalogue toDomain(WatchCatalogueEntity entity) {
        return new WatchCatalogue(
                entity.getId(),
                entity.getName(),
                entity.getUnitPrice(),
                ruleParser.parse(entity.getDiscountExpression())
        );
    }
}
