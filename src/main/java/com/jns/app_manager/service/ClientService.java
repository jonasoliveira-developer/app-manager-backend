package com.jns.app_manager.service;

import com.jns.app_manager.dtos.ClientRequestDTO;
import com.jns.app_manager.dtos.ClientResponseDTO;
import com.jns.app_manager.dtos.mapper.ClientMapper;
import com.jns.app_manager.entity.Client;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserService userService;
    private final ClientMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public ClientResponseDTO findById(UUID id) {
        var client = findClient(id);
        return mapper.toResponse(client);
    }

    public Page<ClientResponseDTO> findAllByUser(UUID userId, String name, Pageable pageable) {
        var nameVerified = name == null ? "" : name;
        var clients = clientRepository.findByUserIdAndNameContainingIgnoreCase(userId,nameVerified, pageable);
        return clients.map(mapper::toResponse);

    }

    public ClientResponseDTO save(ClientRequestDTO dto) {
        var user = userService.findUser(dto.userId());

        var client = mapper.toEntity(dto);
        client.setAccessLevel(AccessLevel.CLIENT);
        client.setPassword(encoder.encode(dto.password()));
        client.setUser(user);
        return mapper.toResponse(clientRepository.save(client));
    }

    public ClientResponseDTO update(UUID id, ClientRequestDTO dto) {
        var client = findClient(id);
        client.setName(dto.name());
        client.setPhoneNumber(dto.phoneNumber());
        client.setAge(dto.age());
        client.setWeight(dto.weight());
        client.setHeight(dto.height());
        client.setLocal(dto.local());
        client.setImageProfile(dto.imageProfile());
        return mapper.toResponse(clientRepository.save(client));
    }

    public void delete(UUID id) {
        var client = findClient(id);
            client.setAccountStatus(AccountStatus.DELETED);
            clientRepository.save(client);
    }

    public Client findClient(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado: " + id));
    }
}