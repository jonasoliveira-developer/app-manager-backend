package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        LocalDate openedDate,
        LocalDate closedDate,
        PaymentStatus paymentStatus,
        UUID carePlanId
) {}