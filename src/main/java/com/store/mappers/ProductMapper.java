package com.store.mappers;

import com.store.dtos.*;
import com.store.entities.Product;
import com.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id",target = "categoryId")
    @Mapping(target = "createdAt", expression ="java(java.time.LocalDateTime.now())")
    ProductDto toDto(Product product);

    Product toEntity(RegisterProductRequest registerProductRequest);
    void update(@MappingTarget UpdateProductRequest updateProductRequest, Product product);

}