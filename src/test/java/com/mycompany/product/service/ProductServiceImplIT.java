package com.mycompany.product.service;

import com.mycompany.product.model.Product;
import com.mycompany.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();  // Clear the repository before each test
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.00));
        testProduct.setDescription("Test Description");
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(BigDecimal.valueOf(150.0));
        newProduct.setDescription("New Product Description");

        Product createdProduct = productService.createProduct(newProduct);

        assertNotNull(createdProduct);
        assertNotNull(newProduct.getId());
        assertEquals(newProduct.getName(), createdProduct.getName());
        assertEquals(newProduct.getPrice(), createdProduct.getPrice());
        assertEquals(newProduct.getDescription(), createdProduct.getDescription());
    }

    @Test
    void testGetProductById() {
        Optional<Product> retrievedProduct = productService.getProductById(testProduct.getId());

        assertTrue(retrievedProduct.isPresent());
        assertEquals(testProduct.getId(), retrievedProduct.get().getId());
        assertEquals(testProduct.getName(), retrievedProduct.get().getName());
        assertEquals(testProduct.getPrice().stripTrailingZeros(), retrievedProduct.get().getPrice().stripTrailingZeros());
        assertEquals(testProduct.getDescription(), retrievedProduct.get().getDescription());
    }

    @Test
    void testGetProductById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Product> retrievedProduct = productService.getProductById(nonExistentId);

        assertFalse(retrievedProduct.isPresent());
    }

    @Test
    void testUpdateProduct() {
        testProduct.setName("Updated Product");
        testProduct.setPrice(BigDecimal.valueOf(200.0));
        testProduct.setDescription("Updated Description");

        Optional<Product> updatedProduct = productService.updateProduct(testProduct);

        assertTrue(updatedProduct.isPresent());
        assertEquals(testProduct.getId(), updatedProduct.get().getId());
        assertEquals(testProduct.getName(), updatedProduct.get().getName());
        assertEquals(testProduct.getPrice(), updatedProduct.get().getPrice());
        assertEquals(testProduct.getDescription(), updatedProduct.get().getDescription());
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setId(UUID.randomUUID());
        nonExistentProduct.setName("Non-existent Product");
        nonExistentProduct.setPrice(BigDecimal.valueOf(200.0));
        nonExistentProduct.setDescription("Non-existent Description");

        Optional<Product> updatedProduct = productService.updateProduct(nonExistentProduct);

        assertFalse(updatedProduct.isPresent());
    }

    @Test
    void testDeleteProduct_ProductExists_ReturnsTrue() {
        UUID productId = testProduct.getId();

        // Act
        boolean result = productService.deleteProduct(productId);

        // Assert
        assertTrue(result, "Product should be deleted successfully");
        Optional<Product> deletedProduct = productRepository.findById(productId);
        assertFalse(deletedProduct.isPresent(), "Product should no longer be present in the repository");
    }

    @Test
    void testDeleteProduct_ProductDoesNotExist_ReturnsFalse() {
        UUID nonExistentProductId = UUID.randomUUID();

        // Act
        boolean result = productService.deleteProduct(nonExistentProductId);

        // Assert
        assertFalse(result, "Attempting to delete a non-existent product should return false");
    }
}
