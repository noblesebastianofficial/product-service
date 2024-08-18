package com.mycompany.product.mapper;

import com.mycompany.product.model.Product;
import com.mycompany.product.openapi.model.ProductDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * DataMapper interface for mapping between Product entities and ProductDto objects.
 * This interface uses MapStruct, a code generator that greatly simplifies the implementation of
 * mappings between Java bean types. By defining mapping methods in this interface, MapStruct
 * will automatically generate the necessary implementation code at compile time.
 */
@Mapper
public interface DataMapper {

    /**
     * An instance of the DataMapper to be used for invoking the mapping methods.
     * MapStruct generates the implementation for this interface and assigns it to this INSTANCE field.
     */
    DataMapper INSTANCE = Mappers.getMapper(DataMapper.class);

    /**
     * Maps a Product entity to a ProductDto.
     *
     * @param product the Product entity to map.
     * @return the mapped ProductDto object.
     */
    ProductDto map(Product product);

    /**
     * Maps a list of Product entities to a list of ProductDto objects.
     *
     * @param products the list of Product entities to map.
     * @return the mapped list of ProductDto objects.
     */
    List<ProductDto> map(List<Product> products);

    /**
     * Maps a ProductDto object to a Product entity.
     *
     * @param productDto the ProductDto to map.
     * @return the mapped Product entity.
     */
    Product map(ProductDto productDto);
}
