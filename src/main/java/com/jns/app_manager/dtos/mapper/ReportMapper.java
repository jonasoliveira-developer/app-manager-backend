package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.ReportRequestDTO;
import com.jns.app_manager.dtos.ReportResponseDTO;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.Report;
import com.jns.app_manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toEntity(ReportRequestDTO dto, Client client, User user) {
        return Report.builder()
                .title(dto.title())
                .councilRegistrationNumber(dto.councilRegistrationNumber())
                .date(dto.date())
                .text(dto.text())
                .client(client)
                .user(user)
                .assignUrlClient(dto.assignUrlClient())
                .assignUrlUser(dto.assignUrlUser())
                .build();
    }

    public ReportResponseDTO toResponse(Report report) {
        return new ReportResponseDTO(
                report.getId(),
                report.getTitle(),
                report.getCouncilRegistrationNumber(),
                report.getDate(),
                report.getText(),
                report.getClient().getId(),
                report.getUser().getId(),
                report.getAssignUrlClient(),
                report.getAssignUrlUser()
        );
    }
}