package com.jns.app_manager.dtos;

import com.jns.app_manager.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CarePlanRequestDTO(
        String title,
        UUID userId,
        UUID clientId,
        UUID paymentId,
        List<Schedule> schedule,
        LocalDate startDate,
        LocalDate expectedEndDate
) {}