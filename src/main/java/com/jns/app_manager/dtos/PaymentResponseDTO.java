package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        String title,
        LocalDate openedDate,
        LocalDate closedDate,
        PaymentStatus paymentStatus,
        UUID carePlanId,
        UUID clientId
) {}