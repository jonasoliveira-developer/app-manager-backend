package com.jns.app_manager.service;

import com.jns.app_manager.dtos.ReportRequestDTO;
import com.jns.app_manager.dtos.ReportResponseDTO;
import com.jns.app_manager.dtos.mapper.ReportMapper;
import com.jns.app_manager.entity.Report;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.ReportRepository;
import com.jns.app_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final ReportMapper reportMapper;

    public ReportResponseDTO findById(UUID id) {
        var report = findReport(id);
        return reportMapper.toResponse(report);
    }

    public Page<ReportResponseDTO> findAllByClientIdAndTitle(UUID clientId, String title, Pageable pageable) {
        String normalizedTitle = title != null ? title : "";
        return reportRepository.findByClientIdAndTitleContainingIgnoreCase(clientId, normalizedTitle, pageable)
                .map(reportMapper::toResponse);
    }

    public ReportResponseDTO save(ReportRequestDTO dto) {
        var client = clientService.findClient(dto.clientId());
        var user = userService.findUser(dto.userId());
        var saved = reportRepository.save(reportMapper.toEntity(dto, client, user));
        return reportMapper.toResponse(saved);
    }

    public ReportResponseDTO update(UUID id, ReportRequestDTO dto) {
        var report = findReport(id);
        report.setTitle(dto.title());
        report.setText(dto.text());
        return reportMapper.toResponse(reportRepository.save(report));
    }

    public void delete(UUID id) {
        var report = findReport(id);
        reportRepository.delete(report);
    }

    public Report findReport(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Relatório não encontrado: " + id));
    }


}