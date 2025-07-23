package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.entity.CarePlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarePlanMapper {

    private final ScheduleMapper scheduleMapper;

    public CarePlanResponseDTO toResponse(CarePlan plan) {
        return new CarePlanResponseDTO(
                plan.getId(),
                plan.getUser().getId(),
                plan.getClient().getId(),
                plan.getStartDate(),
                plan.getExpectedEndDate(),
                plan.getActualEndDate(),
                plan.getPayment() != null ? plan.getPayment().getId() : null,
                plan.getSchedule().stream()
                        .map(scheduleMapper::toResponse)
                        .toList()
        );
    }
}