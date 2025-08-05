package com.jns.app_manager.dtos;

import com.jns.app_manager.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CarePlanResponseDTO(
        String title,
        UUID id,
        UUID userId,
        UUID clientId,
        LocalDate startDate,
        LocalDate expectedEndDate,
        LocalDate actualEndDate,
        UUID paymentId,
        List<Schedule> schedule
) {}