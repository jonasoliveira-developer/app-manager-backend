package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.PaymentRequestDTO;
import com.jns.app_manager.dtos.PaymentResponseDTO;
import com.jns.app_manager.entity.CarePlan;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto,  Client client) {
        return Payment.builder()
                .amount(dto.amount())
                .paymentStatus(dto.paymentStatus())
                .client(client)
                .build();
    }

    public PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getOpenedDate(),
                payment.getClosedDate(),
                payment.getPaymentStatus(),
                payment.getClient().getId()
        );
    }
}