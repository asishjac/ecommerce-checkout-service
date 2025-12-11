package com.handelsbanken.ecommerce.checkout_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watch_catalogue")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WatchCatalogueEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    // "3 for 200" or null
    @Column(name = "discount_expression")
    private String discountExpression;
}
