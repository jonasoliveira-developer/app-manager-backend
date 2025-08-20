package com.jns.app_manager.dtos;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String councilRegistrationNumber,
        String subscriptionType
) {}