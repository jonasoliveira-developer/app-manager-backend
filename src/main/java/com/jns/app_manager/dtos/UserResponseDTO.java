package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String councilRegistrationNumber,
        String subscriptionType,
        AccountStatus accountStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}