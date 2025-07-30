package com.jns.app_manager.controller;

import com.jns.app_manager.dtos.ReportRequestDTO;
import com.jns.app_manager.dtos.ReportResponseDTO;
import com.jns.app_manager.service.ReportPdfService;
import com.jns.app_manager.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportPdfService pdfService;


    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> findById(@PathVariable UUID id) {
        var response = reportService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> findAll() {
        var response = reportService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReportResponseDTO> save(@RequestBody @Valid ReportRequestDTO dto) {
        var response = reportService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid ReportRequestDTO dto
    ) {
        var response = reportService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }


    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable UUID id) {
        byte[] pdfBytes = pdfService.generatePdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + id + ".pdf")
                .body(pdfBytes);
    }


}