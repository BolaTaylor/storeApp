package com.store.controllers;

import com.store.dtos.RegisterUserRequest;
import com.store.dtos.UpdateUserRequest;
import com.store.dtos.UserDto;
import com.store.entities.User;
import com.store.mappers.UserMapper;
import com.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/add")
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest registerUserRequest,
                                              UriComponentsBuilder uriComponentsBuilder) {
        var user = userMapper.toEntity(registerUserRequest);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userMapper.update(updateUserRequest, user);
        var savedUserDto = userMapper.toDto(user);
        return ResponseEntity.ok(savedUserDto);        // 3. Returns the response

      }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
