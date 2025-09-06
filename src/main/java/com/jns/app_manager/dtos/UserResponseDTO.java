package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String councilRegistrationNumber,
        String imageProfile,
        String subscriptionType,
        AccountStatus accountStatus
) {}