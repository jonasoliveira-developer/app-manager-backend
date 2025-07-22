package com.jns.app_manager.service;

import com.jns.app_manager.dtos.ClientRequestDTO;
import com.jns.app_manager.dtos.ClientResponseDTO;
import com.jns.app_manager.dtos.mapper.ClientMapper;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper mapper;

    public ClientResponseDTO findById(UUID id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));

        return mapper.toResponse(client);
    }

    public List<ClientResponseDTO> findAllByUser(UUID userId) {
        var clients = clientRepository.findAllByUserId(userId);
        return clients.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ClientResponseDTO save(ClientRequestDTO dto) {
        var client = mapper.toEntity(dto);
        return mapper.toResponse(clientRepository.save(client));
    }

    public ClientResponseDTO update(UUID id, ClientRequestDTO dto) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));

        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPassword(dto.password());
        client.setPhoneNumber(dto.phoneNumber());
        client.setAge(dto.age());
        client.setWeight(dto.weight());
        client.setHeight(dto.height());
        client.setLocal(dto.local());
        client.setImageProfile(dto.imageProfile());

        return mapper.toResponse(clientRepository.save(client));
    }

    public void delete(UUID id) {
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Client not found"));

        clientRepository.delete(client);
    }
}