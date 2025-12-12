package com.handelsbanken.ecommerce.checkout_service.util;

import com.handelsbanken.ecommerce.checkout_service.domain.discount.BulkDiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.DiscountStrategy;
import com.handelsbanken.ecommerce.checkout_service.domain.discount.NoDiscountStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountRuleParserTest {

    private final DiscountRuleParser discountRuleParser = new DiscountRuleParser();

    @Test
    void parse_parsesBulkRule() {
        DiscountStrategy strategy = discountRuleParser.parse("3 for 200");
        assertInstanceOf(BulkDiscountStrategy.class, strategy);
    }

    @Test
    void parse_RejectsUnknownRules() {
        assertThrows(IllegalArgumentException.class, () -> discountRuleParser.parse("buy one get one"));
    }

    @Test
    void parse_ReturnsNoDiscount_ForNullOrBlank() {
        DiscountStrategy s1 = discountRuleParser.parse(null);
        DiscountStrategy s2 = discountRuleParser.parse("   ");
        DiscountStrategy s3 = discountRuleParser.parse("");
        assertInstanceOf(NoDiscountStrategy.class, s1);
        assertInstanceOf(NoDiscountStrategy.class, s2);
        assertInstanceOf(NoDiscountStrategy.class, s3);
    }

    @Test
    void rejectsNonPositiveNumbers() {
        assertThrows(IllegalArgumentException.class, () -> discountRuleParser.parse("-1 for 100"));
        assertThrows(IllegalArgumentException.class, () -> discountRuleParser.parse("2 for 0"));
        assertThrows(IllegalArgumentException.class, () -> discountRuleParser.parse("0 for 1"));
    }
}
