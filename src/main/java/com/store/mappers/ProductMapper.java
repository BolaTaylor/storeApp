package com.store.mappers;

import com.store.dtos.ProductDto;
import com.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id",target = "categoryId")
    @Mapping(target = "createdAt", expression ="java(java.time.LocalDateTime.now())")
    ProductDto toDto(Product product);
}