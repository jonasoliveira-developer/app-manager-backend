package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentRequestDTO(
        String title,
        LocalDate openedDate,
        PaymentStatus paymentStatus,
        UUID carePlanId,
        UUID clientId
) {}