package com.store.mappers;

import com.store.dtos.UserDto;
import com.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}