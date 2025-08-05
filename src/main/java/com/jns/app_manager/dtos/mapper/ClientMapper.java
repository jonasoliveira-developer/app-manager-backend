package com.jns.app_manager.dtos.mapper;


import com.jns.app_manager.dtos.ClientRequestDTO;
import com.jns.app_manager.dtos.ClientResponseDTO;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    public Client toEntity(ClientRequestDTO dto) {
        return Client.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .phoneNumber(dto.phoneNumber())
                .age(dto.age())
                .weight(dto.weight())
                .height(dto.height())
                .local(dto.local())
                .imageProfile(dto.imageProfile())
                .accountStatus(AccountStatus.ACTIVE)
                .build();
    }

    public ClientResponseDTO toResponse(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getAge(),
                client.getWeight(),
                client.getHeight(),
                client.getLocal(),
                client.getAccountStatus(),
                client.getImageProfile(),
                client.getUser().getId()
        );
    }
}