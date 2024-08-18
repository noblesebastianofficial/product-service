package com.mycompany.product.service;

import com.mycompany.product.model.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@code ProductService} interface defines the contract for managing products.
 * <p>
 * This interface provides methods for CRUD (Create, Retrieve, Update, Delete) operations
 * on {@link Product} entities. It also supports pagination for retrieving multiple products.
 * </p>
 */
public interface ProductService {

    /**
     * Retrieves all products with pagination support.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of {@link Product} objects
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the UUID of the product to retrieve
     * @return an {@link Optional} containing the found {@link Product}, or empty if not found
     */
    Optional<Product> getProductById(UUID id);

    /**
     * Creates a new product and saves it to the repository.
     *
     * @param product the product to create
     * @return the created {@link Product}
     */
    Product createProduct(Product product);

    /**
     * Updates an existing product.
     * If the product is found, it is updated and saved to the repository.
     * If not found, an empty {@link Optional} is returned.
     *
     * @param updatedProduct the product containing updated information
     * @return an {@link Optional} containing the updated {@link Product}, or empty if not found
     */
    Optional<Product> updateProduct(Product updatedProduct);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the UUID of the product to delete
     */
    public boolean deleteProduct(UUID id);
}
