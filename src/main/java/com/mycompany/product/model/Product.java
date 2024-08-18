package com.mycompany.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code Product} class represents a product entity in the system.
 * <p>
 * Each product has a unique identifier, name, description, price, and version for
 * optimistic locking. This entity is mapped to a database table named {@code Product}.
 * </p>
 */
@Slf4j
@Data
@Entity
public class Product {

    /**
     * The unique identifier for the product.
     * This ID is automatically generated using UUID strategy.
     */
    @NotNull
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * The name of the product.
     * This field is mandatory.
     */
    @NotNull
    private String name;

    /**
     * The description of the product.
     * This field is mandatory.
     */
    @NotNull
    private String description;

    /**
     * The price of the product.
     * This field is mandatory.
     */
    @NotNull
    private BigDecimal price;

    /**
     * The version number for optimistic locking.
     * This field is managed by the persistence provider.
     */
    @NotNull
    @Version
    private long version;

}
