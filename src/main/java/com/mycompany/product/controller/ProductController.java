package com.mycompany.product.controller;

import com.mycompany.product.mapper.DataMapper;
import com.mycompany.product.model.Product;
import com.mycompany.product.openapi.api.ProductsApi;
import com.mycompany.product.openapi.model.ProductDto;
import com.mycompany.product.service.ProductService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


/**
 * ProductController implements the ProductsApi interface and handles HTTP requests
 * related to product operations. It provides endpoints to create, retrieve, update,
 * and delete products, while managing UUID parsing and exception handling centrally.
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class ProductController implements ProductsApi {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param productDto The details of the product to create.
     * @return ResponseEntity with the created ProductDto and a 201 Created status.
     */
    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        log.info("Creating product with details: {}", productDto);
        Product savedProduct = productService.createProduct(DataMapper.INSTANCE.map(productDto));
        log.info("Product created with ID: {}", savedProduct.getId());
        return ResponseEntity.ok(DataMapper.INSTANCE.map(savedProduct));
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The UUID of the product to delete.
     * @return ResponseEntity with a 204 No Content status if deletion is successful.
     *         Returns a 400 Bad Request if the UUID is invalid.
     */
    @Override
    public ResponseEntity<Void> deleteProduct(UUID id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            log.info("Product with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Product with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Retrieves all products.
     *
     * @return ResponseEntity with a list of ProductDto and a 200 OK status.
     */
    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(Integer firstResult, Integer maxResults) {
        log.info("Fetching all products");
        Pageable pageable = PageRequest.of(firstResult, maxResults);
        Page<Product> productPage = productService.getAllProducts(pageable);
        log.info("Fetched {} products", productPage.getContent().size());
        return ResponseEntity.ok(DataMapper.INSTANCE.map(productPage.getContent()));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The UUID of the product to retrieve.
     * @return ResponseEntity with the ProductDto if found, and a 200 OK status.
     *         Returns a 404 Not Found if the product does not exist.
     *         Returns a 400 Bad Request if the UUID is invalid.
     */
    @Override
    public ResponseEntity<ProductDto> getProductById(UUID id) {
        return productService.getProductById(id)
            .map(product -> ResponseEntity.ok(DataMapper.INSTANCE.map(product)))
            .orElseGet(() -> {
                log.warn("Product with ID: {} not found", id);
                return ResponseEntity.notFound().build();
            });
    }

    /**
     * Updates an existing product.
     *
     * @param productDto The updated product details.
     * @return ResponseEntity with the updated ProductDto and a 200 OK status if successful.
     *         Returns a 404 Not Found if the product does not exist.
     *         Returns a 400 Bad Request if the UUID is invalid.
     */
    @Override
    public ResponseEntity<ProductDto> updateProduct(ProductDto productDto) {

        Product product = DataMapper.INSTANCE.map(productDto);
        return productService.updateProduct(product)
            .map(updatedProduct -> ResponseEntity.ok(DataMapper.INSTANCE.map(updatedProduct)))
            .orElseGet(() -> {
                log.warn("Product with ID: {} not found for update", productDto.getId());
                return ResponseEntity.notFound().build();  // 404 Not Found
            });

    }

}