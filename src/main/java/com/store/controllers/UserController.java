package com.store.controllers;

import com.store.dtos.UserDto;
import com.store.entities.User;
import com.store.mappers.UserMapper;
import com.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @GetMapping("/all")
    Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/sort")
    Iterable<UserDto> getAllUsersSort(
            @RequestParam String sort
    ) {
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(userMapper.toDto(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
