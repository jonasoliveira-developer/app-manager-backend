package com.jns.app_manager.dtos;

import java.util.UUID;

public record ScheduleResponseDTO(
        UUID id,
        String dayOfWeek,
        String sessionTime,
        String color,
        UUID carePlanId
) {}