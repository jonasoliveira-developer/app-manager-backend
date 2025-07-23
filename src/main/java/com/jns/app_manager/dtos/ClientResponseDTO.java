package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.AccountStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String age,
        String weight,
        String height,
        String local,
        AccountStatus accountStatus,
        String imageProfile,
        UUID userId
) {}