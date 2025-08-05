package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.PaymentRequestDTO;
import com.jns.app_manager.dtos.PaymentResponseDTO;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto, CarePlan carePlan, Client client) {
        return Payment.builder()
                .title(dto.title())
                .openedDate(dto.openedDate())
                .paymentStatus(dto.paymentStatus())
                .carePlan(carePlan)
                .client(client)
                .build();
    }

    public PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getTitle(),
                payment.getOpenedDate(),
                payment.getClosedDate(),
                payment.getPaymentStatus(),
                payment.getCarePlan().getId(),
                payment.getClient().getId()
        );
    }
}