package com.store.controllers;

import com.store.dtos.*;
import com.store.entities.Product;
import com.store.mappers.ProductMapper;
import com.store.repositories.CategoryRepository;
import com.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createNewProduct(@RequestBody RegisterProductRequest registerProductRequest,
                                              UriComponentsBuilder uriComponentsBuilder) {

        var category = categoryRepository.findById(Byte.valueOf(registerProductRequest.getCategoryId())).orElseThrow();
        var product = productMapper.toEntity(registerProductRequest);
       product.setCategory(category);
        productRepository.save(product);
        var productDto = productMapper.toDto(product);
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        productMapper.update(updateProductRequest, product);
        var savedProductDto = productMapper.toDto(product);
        return ResponseEntity.ok(savedProductDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


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
