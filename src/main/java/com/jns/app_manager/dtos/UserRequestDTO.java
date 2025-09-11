package com.jns.app_manager.dtos;

import com.jns.app_manager.enums.SubscriptionType;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(

        String name,

        String email,

        String password,


        String phoneNumber,

        String councilRegistrationNumber,

        String subscriptionType,

        @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
        String biography,

        @Size(max = 1000, message = "O campo 'Sobre mim' deve ter no máximo 1000 caracteres")
        String aboutMe
) {}