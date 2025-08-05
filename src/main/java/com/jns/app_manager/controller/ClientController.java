
package com.jns.app_manager.controller;


import com.jns.app_manager.dtos.ClientRequestDTO;
import com.jns.app_manager.dtos.ClientResponseDTO;
import com.jns.app_manager.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> listClients(
            @RequestParam UUID userId,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<ClientResponseDTO> page = service.findAllByUser(userId, name, pageable);
        return ResponseEntity.ok(page);
    }



    @PostMapping
    public ResponseEntity<ClientResponseDTO> save(@RequestBody @Valid ClientRequestDTO dto) {
        var saved = service.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable UUID id,
                                                    @RequestBody @Valid ClientRequestDTO dto) {
        var updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}