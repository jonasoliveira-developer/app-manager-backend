package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.ScheduleRequestDTO;
import com.jns.app_manager.dtos.ScheduleResponseDTO;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleResponseDTO toResponse(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getDayOfWeek(),
                schedule.getSessionTime(),
                schedule.getColor(),
                schedule.getCarePlan().getId()
        );
    }

    public Schedule toEntity(ScheduleRequestDTO dto, CarePlan carePlan) {
        return Schedule.builder()
                .dayOfWeek(dto.dayOfWeek())
                .sessionTime(dto.sessionTime())
                .color(dto.color())
                .carePlan(carePlan)
                .build();
    }
}