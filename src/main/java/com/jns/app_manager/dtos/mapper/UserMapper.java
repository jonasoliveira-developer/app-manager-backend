package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.UserRequestDTO;
import com.jns.app_manager.dtos.UserResponseDTO;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.enums.SubscriptionType;
import org.springframework.stereotype.Component;

@Component
public  class UserMapper  {

    public  UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCouncilRegistrationNumber(),
                user.getSubscriptionType().name(),
                user.getAccountStatus()
        );
    }

    public  User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .phoneNumber(dto.phoneNumber())
                .councilRegistrationNumber(dto.councilRegistrationNumber())
                .subscriptionType(SubscriptionType.valueOf(dto.subscriptionType()))
                .imageProfile(dto.imageProfile())
                .build();
    }
}