package com.handelsbanken.ecommerce.checkout_service.repository;

import com.handelsbanken.ecommerce.checkout_service.entity.WatchCatalogueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchCatalogueRepository extends JpaRepository<WatchCatalogueEntity, String> {
}
