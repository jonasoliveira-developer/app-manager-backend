package com.jns.app_manager.controller;


import com.jns.app_manager.dtos.CarePlanRequestDTO;
import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.service.CarePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/care-plans")
@RequiredArgsConstructor
public class CarePlanController {

    private final CarePlanService service;


    @PostMapping
    public ResponseEntity<CarePlanResponseDTO> save(@RequestBody CarePlanRequestDTO dto) {
        var response = service.save(dto);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<CarePlanResponseDTO> findById(@PathVariable UUID id) {
        var response = service.findById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("list/{id}")
    public ResponseEntity<List<CarePlanResponseDTO>> findAllByUser(@PathVariable UUID id) {
        var response = service.findAllCarePlansByUserOrClientId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}