package com.jns.app_manager.controller;


import com.jns.app_manager.dtos.CarePlanRequestDTO;
import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.service.CarePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    @GetMapping
    public ResponseEntity<Page<CarePlanResponseDTO>> getCarePlans(
            @RequestParam UUID id,
            @RequestParam(required = false) String title,
            Pageable pageable) {

        Page<CarePlanResponseDTO> page = service.findCarePlansByUserOrClientIdAndTitle(id, title, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}