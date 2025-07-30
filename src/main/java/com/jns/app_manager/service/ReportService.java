package com.jns.app_manager.service;

import com.jns.app_manager.dtos.ReportRequestDTO;
import com.jns.app_manager.dtos.ReportResponseDTO;
import com.jns.app_manager.dtos.mapper.ReportMapper;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.ReportRepository;
import com.jns.app_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    public ReportResponseDTO findById(UUID id) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Report not found"));
        return reportMapper.toResponse(report);
    }

    public List<ReportResponseDTO> findAll() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ReportResponseDTO save(ReportRequestDTO dto) {
        var client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));

        var user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        var saved = reportRepository.save(reportMapper.toEntity(dto, client, user));

        return reportMapper.toResponse(saved);
    }

    public ReportResponseDTO update(UUID id, ReportRequestDTO dto) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Report not found"));

        report.setText(dto.text());

        return reportMapper.toResponse(reportRepository.save(report));
    }

    public void delete(UUID id) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Report not found"));
        reportRepository.delete(report);
    }
}