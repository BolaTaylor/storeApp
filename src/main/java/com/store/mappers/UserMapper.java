package com.store.mappers;

import com.store.dtos.RegisterUserRequest;
import com.store.dtos.UpdateUserRequest;
import com.store.dtos.UserDto;
import com.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression ="java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest  registerUserRequest);
    void update(@MappingTarget UpdateUserRequest updateUserRequest, User user);
}