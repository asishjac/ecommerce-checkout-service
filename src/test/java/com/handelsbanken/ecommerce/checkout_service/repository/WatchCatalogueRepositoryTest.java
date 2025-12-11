package com.handelsbanken.ecommerce.checkout_service.repository;

import com.handelsbanken.ecommerce.checkout_service.entity.WatchCatalogueEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class WatchCatalogueRepositoryTest {

    @Autowired
    private WatchCatalogueRepository watchCatalogueRepository;

    @Test
    void findById_ShouldReturnWatch_WhenExists() {

        Optional<WatchCatalogueEntity> result = watchCatalogueRepository.findById("001");
        assertTrue(result.isPresent());
        WatchCatalogueEntity watch = result.get();
        assertEquals("Rolex", watch.getName());
        assertEquals("3 for 200", watch.getDiscountExpression());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenDoesNotExist() {
        Optional<WatchCatalogueEntity> result = watchCatalogueRepository.findById("999");
        assertTrue(result.isEmpty());
    }

}
