package com.store.controllers;

import com.store.dtos.ProductDto;
import com.store.entities.Product;
import com.store.mappers.ProductMapper;
import com.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(@RequestParam(name="categoryId",
                                            required = false)
                                            Byte categoryId) {
        List <Product> products = new ArrayList<>();
        if(categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
         } else {
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper :: toDto).toList();
    }

    @GetMapping("/sort")
    List<ProductDto> getAllProductsSort(
            @RequestParam String sort
    ) {
        return productRepository.findAll(Sort.by(sort))
                .stream()
                .map(productMapper::toDto)
                .toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
