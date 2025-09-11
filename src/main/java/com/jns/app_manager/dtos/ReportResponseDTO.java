package com.jns.app_manager.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponseDTO(
        UUID id,
        String title,
        String councilRegistrationNumber,
        LocalDate date,
        String text,
        UUID clientId,
        UUID userId,
        String assignUrlClient,
        String assignUrlUser
) {}