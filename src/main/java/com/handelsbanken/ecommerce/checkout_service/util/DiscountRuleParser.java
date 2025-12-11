package com.handelsbanken.ecommerce.checkout_service.util;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class DiscountRuleParser {

    private static final Pattern BULK_PATTERN = Pattern.compile("^(\\d+)\\s+for\\s+(\\d+)$");

    public DiscountStrategy parse(String expression) {
        if (expression == null || expression.isBlank()) {
            return new NoDiscountStrategy();
        }

        Matcher matcher = BULK_PATTERN.matcher(expression.trim());
        if (matcher.matches()) {
            int quantity = Integer.parseInt(matcher.group(1));
            int price = Integer.parseInt(matcher.group(2));
            if (quantity <= 0 || price <= 0) {
                throw new IllegalArgumentException("Quantity or price must be positive: " + expression);
            }
            return new BulkDiscountStrategy(quantity, price);
        }

        throw new IllegalArgumentException("Unknown discount rule format: " + expression);
    }
}
