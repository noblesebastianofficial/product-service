package com.mycompany.product.service;

import com.mycompany.product.model.Product;
import com.mycompany.product.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing products.
 *This class provides methods to create, retrieve, update, and delete products.
 * It acts as a bridge between the controller layer and the data repository,
 * encapsulating the business logic and providing centralized exception handling.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @NonNull
    private final ProductRepository productRepository;

    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all products
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Retrieving all products");
        return productRepository.findAll(pageable);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the UUID of the product to retrieve
     * @return an Optional containing the found product, or empty if not found
     */
    public Optional<Product> getProductById(UUID id) {
        log.info("Retrieving product with ID: {}", id);
        return productRepository.findById(id);
    }

    /**
     * Creates a new product and saves it to the repository.
     *
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(Product product) {
        log.info("Creating new product with name: {}", product.getName());
        return productRepository.save(product);
    }

    /**
     * Updates an existing product by its unique identifier.
     * If the product is found, its name and price are updated, and the updated product is saved to the repository.
     * If the product is not found, an empty Optional is returned.
     *
     * @param updatedProduct the product containing updated information
     * @return an Optional containing the updated product, or empty if not found
     */
    public Optional<Product> updateProduct(Product updatedProduct) {
        log.info("Updating product with ID: {}", updatedProduct.getId());
        return productRepository.findById(updatedProduct.getId())
            .map(existingProduct -> {
                log.info("Product found. Updating product with ID: {}", updatedProduct.getId());
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setDescription(updatedProduct.getDescription());
                existingProduct.setPrice(updatedProduct.getPrice());
                return Optional.of(productRepository.save(existingProduct));
            }).orElseGet(() -> {
                log.warn("Product with ID: {} not found", updatedProduct.getId());
                return Optional.empty();
            });
    }

    /**
     * Deletes a product by its unique identifier.
     * If the product is not found, a RuntimeException is thrown.
     *
     * @param id the UUID of the product to delete
     */
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}