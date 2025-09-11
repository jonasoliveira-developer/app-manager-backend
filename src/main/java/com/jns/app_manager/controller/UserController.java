package com.jns.app_manager.controller;

import com.jns.app_manager.dtos.UserRequestDTO;
import com.jns.app_manager.dtos.UserResponseDTO;
import com.jns.app_manager.dtos.mapper.UpdatePasswordRequest;
import com.jns.app_manager.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO dto) throws MessagingException {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable UUID id, @RequestBody @Valid UpdatePasswordRequest dto) {
       service.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}