package com.jns.app_manager.dtos;

import java.util.UUID;

public record ScheduleRequestDTO(
        String dayOfWeek,
        String sessionTime,
        UUID carePlanId
) {}