package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentRequestDTO(
        BigDecimal amount,
        PaymentStatus paymentStatus,
        UUID carePlanId
) {}