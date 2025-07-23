package com.jns.app_manager.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CarePlanResponseDTO(
        UUID id,
        UUID userId,
        UUID clientId,
        LocalDate startDate,
        LocalDate expectedEndDate,
        LocalDate actualEndDate,
        UUID paymentId,
        List<ScheduleResponseDTO> schedule
) {}