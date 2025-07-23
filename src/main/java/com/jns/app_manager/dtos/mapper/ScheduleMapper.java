package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.ScheduleResponseDTO;
import com.jns.app_manager.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleResponseDTO toResponse(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getDayOfWeek(),
                schedule.getSessionTime(),
                schedule.getCarePlan().getId()
        );
    }
}