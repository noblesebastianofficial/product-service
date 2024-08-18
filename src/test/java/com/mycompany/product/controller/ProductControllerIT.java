package com.mycompany.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.product.openapi.model.ProductDto;
import com.mycompany.product.model.Product;
import com.mycompany.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {

    private static final String BASE_URL = "/v1/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto testProductDto;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();  // Clear the repository before each test
        Product testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.0));
        testProduct.setDescription("Test Description");
        productRepository.save(testProduct);

        testProductDto = new ProductDto();
        testProductDto.setId(testProduct.getId());
        testProductDto.setName(testProduct.getName());
        testProductDto.setPrice(testProduct.getPrice());
        testProductDto.setDescription(testProduct.getDescription());
    }


    @Test
    void testCreateProduct() throws Exception {
        ProductDto newProductDto = new ProductDto();
        newProductDto.setName("New Product");
        newProductDto.setPrice(BigDecimal.valueOf(150.0));
        newProductDto.setDescription("New Product Description");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProductDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(newProductDto.getName()))
            .andExpect(jsonPath("$.price").value(newProductDto.getPrice().toString()))
            .andExpect(jsonPath("$.description").value(newProductDto.getDescription()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        UUID productId = testProductDto.getId();
        mockMvc.perform(delete(BASE_URL + "/" + productId))
            .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllProducts() throws Exception {

        mockMvc.perform(get(BASE_URL)
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").isNotEmpty())
            .andExpect(jsonPath("$[0].name").value(testProductDto.getName()))
            .andExpect(jsonPath("$[0].price").value(testProductDto.getPrice().toString()))
            .andExpect(jsonPath("$[0].description").value(testProductDto.getDescription()));
    }

    @Test
    void testGetProductById() throws Exception {
        UUID productId = testProductDto.getId();

        mockMvc.perform(get(BASE_URL + "/" + productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(testProductDto.getName()))
            .andExpect(jsonPath("$.price").value(testProductDto.getPrice().toString()))
            .andExpect(jsonPath("$.description").value(testProductDto.getDescription()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        UUID productId = testProductDto.getId();
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(productId);
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setPrice(BigDecimal.valueOf(150.0));
        updatedProductDto.setDescription("Updated Product Description");

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(updatedProductDto.getName()))
            .andExpect(jsonPath("$.price").value(updatedProductDto.getPrice().toString()))
            .andExpect(jsonPath("$.description").value(updatedProductDto.getDescription()));
    }

    // Negative test cases...

    @Test
    void testCreateProductWithMissingFields() throws Exception {
        ProductDto newProductDto = new ProductDto();
        newProductDto.setPrice(BigDecimal.valueOf(150.0));  // Missing name and description

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProductDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProductWithInvalidPrice() throws Exception {
        ProductDto newProductDto = new ProductDto();
        newProductDto.setName("New Product");
        newProductDto.setPrice(BigDecimal.valueOf(-10.0));  // Invalid negative price
        newProductDto.setDescription("New Product Description");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProductDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductByInvalidId() throws Exception {
        UUID invalidProductId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/" + invalidProductId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductByInvalidId() throws Exception {
        UUID invalidProductId = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/" + invalidProductId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProductWithInvalidId() throws Exception {
        UUID invalidProductId = UUID.randomUUID();
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(invalidProductId);
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setPrice(BigDecimal.valueOf(150.0));
        updatedProductDto.setDescription("Updated Product Description");

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProductWithMissingFields() throws Exception {
        UUID productId = testProductDto.getId();
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(productId);
        updatedProductDto.setName(null);  // Missing name
        updatedProductDto.setPrice(BigDecimal.valueOf(150.0));
        updatedProductDto.setDescription("Updated Product Description");

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDto)))
            .andExpect(status().isBadRequest());
    }
}
