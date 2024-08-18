package com.mycompany.product.service;

import com.mycompany.product.model.Product;
import com.mycompany.product.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.0));
        testProduct.setDescription("Test Description");
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct), pageable, 1);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<Product> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProduct.getName(), result.getContent().getFirst().getName());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetProductById() {
        UUID productId = testProduct.getId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProductById(productId);

        assertTrue(result.isPresent());
        assertEquals(testProduct.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(productId);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testUpdateProduct() {
        UUID productId = testProduct.getId();
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(BigDecimal.valueOf(150.0));
        updatedProduct.setDescription("Updated Description");

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Optional<Product> result = productService.updateProduct(updatedProduct);

        assertTrue(result.isPresent());
        assertEquals("Updated Product", result.get().getName());
        assertEquals(BigDecimal.valueOf(150.0), result.get().getPrice());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        UUID productId = UUID.randomUUID();
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(updatedProduct);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_WhenProductExists() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(true);

        boolean result = productService.deleteProduct(productId);

        assertTrue(result, "Product should be deleted and return true");
        verify(productRepository).deleteById(productId);
        verify(productRepository).existsById(productId);
    }

    @Test
    void testDeleteProduct_WhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        when(productRepository.existsById(productId)).thenReturn(false);

        boolean result = productService.deleteProduct(productId);

        assertFalse(result, "Product should not be deleted and return false");
        verify(productRepository, never()).deleteById(productId);
        verify(productRepository).existsById(productId);
    }
}
