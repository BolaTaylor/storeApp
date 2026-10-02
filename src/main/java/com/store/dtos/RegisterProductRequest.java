package com.store.dtos;

import lombok.Data;

@Data
public class RegisterProductRequest {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String categoryId;

}
