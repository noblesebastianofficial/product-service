package com.mycompany.product.repository;

import com.mycompany.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@SpringJUnitConfig
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.0));
        testProduct.setDescription("Test Description");
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void testFindById() {
        UUID productId = testProduct.getId();
        Optional<Product> foundProduct = productRepository.findById(productId);

        assertTrue(foundProduct.isPresent());
        assertEquals(testProduct.getName(), foundProduct.get().getName());
    }

    @Test
    void testSaveProduct() {
        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName("New Product");
        newProduct.setPrice(BigDecimal.valueOf(150.0));
        newProduct.setDescription("New Product Description");

        Product savedProduct = productRepository.save(newProduct);

        assertNotNull(savedProduct.getId());
        assertEquals(newProduct.getName(), savedProduct.getName());
        assertEquals(newProduct.getPrice(), savedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        UUID productId = testProduct.getId();
        productRepository.deleteById(productId);
        Optional<Product> deletedProduct = productRepository.findById(productId);

        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testFindAll() {
        Product anotherProduct = new Product();
        anotherProduct.setId(UUID.randomUUID());
        anotherProduct.setName("Another Product");
        anotherProduct.setPrice(BigDecimal.valueOf(200.0));
        anotherProduct.setDescription("Another Description");
        productRepository.save(anotherProduct);

        Iterable<Product> products = productRepository.findAll();

        assertTrue(products.iterator().hasNext());
    }

}
