package com.store.dtos;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;
    private String email;
    private String password;
  }
