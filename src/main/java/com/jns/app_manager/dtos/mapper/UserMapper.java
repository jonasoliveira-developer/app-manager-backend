package com.jns.app_manager.dtos.mapper;

import com.jns.app_manager.dtos.UserRequestDTO;
import com.jns.app_manager.dtos.UserResponseDTO;
import com.jns.app_manager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class UserMapper  {
    private final ClientMapper clientMapper;

    public  UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCouncilRegistrationNumber(),
                user.getSubscriptionType().name(),
                user.getAccountStatus(),
                user.getBiography(),
                user.getAboutMe(),
                user.getImageProfile(),
                user.getImageMimeType(),

                user.getImageUrl()




        );
    }

    public  User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .phoneNumber(dto.phoneNumber())
                .councilRegistrationNumber(dto.councilRegistrationNumber())
                .build();
    }
}