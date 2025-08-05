package com.jns.app_manager.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record ReportRequestDTO(
        String title,
        String councilRegistrationNumber,
        LocalDate date,
        String text,
        UUID clientId,
        UUID userId
) {}