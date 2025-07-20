package com.jns.app_manager.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleResponseDTO(
        UUID id,
        String dayOfWeek,
        String sessionTime,
        UUID carePlanId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}