package com.jns.app_manager.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record CarePlanRequestDTO(
        UUID userId,
        UUID clientId,
        UUID paymentId,
        LocalDate startDate,
        LocalDate expectedEndDate
) {}