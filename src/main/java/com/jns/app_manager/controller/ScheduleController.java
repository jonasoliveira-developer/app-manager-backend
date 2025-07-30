package com.jns.app_manager.controller;

import com.jns.app_manager.dtos.ScheduleRequestDTO;
import com.jns.app_manager.dtos.ScheduleResponseDTO;
import com.jns.app_manager.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> findById(@PathVariable UUID id) {
        var response = scheduleService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> save(@RequestBody ScheduleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody ScheduleRequestDTO dto
    ) {
        var response = scheduleService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}