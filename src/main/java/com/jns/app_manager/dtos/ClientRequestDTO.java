package com.jns.app_manager.dtos;

public record ClientRequestDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String age,
        String weight,
        String height,
        String local,
        String imageProfile
) {}