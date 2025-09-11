package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.AccountStatus;
import com.jns.app_manager.enums.SubscriptionType;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String councilRegistrationNumber,
        String subscriptionType,
        AccountStatus accountStatus,
        String biography,
        String aboutMe,
        byte[] imageProfile,
        String imageMimeType,
        String imageUrl
) {

}