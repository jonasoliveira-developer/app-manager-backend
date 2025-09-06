package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.CarePlanRequestDTO;
import com.jns.app_manager.dtos.CarePlanResponseDTO;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.Payment;
import com.jns.app_manager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarePlanMapper {

    public CarePlanResponseDTO toResponse(CarePlan plan) {
        return new CarePlanResponseDTO(
                plan.getTitle(),
                plan.getId(),
                plan.getUser().getId(),
                plan.getClient().getId(),
                plan.getPaymentId(),
                plan.getStartDate(),
                plan.getExpectedEndDate(),
                plan.getActualEndDate(),
                plan.getSchedule()
        );
    }


    public CarePlan toEntity(CarePlanRequestDTO dto, User user, Client client, Payment payment) {
        return CarePlan.builder()
                .title(dto.title())
                .user(user)
                .client(client)
                .paymentId(String.valueOf(payment.getId()))
                .startDate(dto.startDate())
                .expectedEndDate(dto.expectedEndDate())
                .schedule(dto.schedule())
                .build();
    }


}