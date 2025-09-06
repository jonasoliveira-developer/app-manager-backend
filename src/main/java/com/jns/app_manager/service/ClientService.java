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
import com.jns.app_manager.utils.PasswordGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserService userService;
    private final ClientMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    public ClientResponseDTO findById(UUID id) {
        var client = findClient(id);
        return mapper.toResponse(client);
    }

    public Page<ClientResponseDTO> findAllByUser(UUID userId, String name, Pageable pageable) {
        var nameVerified = name == null ? "" : name;
        var clients = clientRepository.findByUserIdAndNameContainingIgnoreCase(userId,nameVerified, pageable);
        return clients.map(mapper::toResponse);

    }

    public ClientResponseDTO save(ClientRequestDTO dto) throws MessagingException {
        var user = userService.findUser(dto.userId());

        var client = mapper.toEntity(dto);
        var password = PasswordGenerator.generate(8);

        client.setPassword(encoder.encode(password));
        client.setAccessLevel(AccessLevel.CLIENT);
        client.setAccountStatus(AccountStatus.ACTIVE);
        client.setUser(user);

        String bodyEmailMessage = String.format(
                "Olá <strong>%s</strong>,<br><br>" +
                        "Seja bem-vindo(a) ao <strong>FisioAdmin</strong>, sua plataforma de gestão inteligente.<br><br>" +
                        "Você foi cadastrado como cliente vinculado ao profissional <strong>%s</strong>.<br><br>" +
                        "Abaixo estão suas credenciais de acesso. Recomendamos que personalize sua senha após o primeiro login.<br><br>" +
                        "Estamos felizes em tê-lo(a) conosco!",
                client.getName(),
                user.getName()
        );

        try {
            emailService.sendTemplatedEmail(
                    client.getEmail(),
                    "Credenciais de acesso",
                    bodyEmailMessage,
                    "FisioAdmin",
                    password
            );
        } catch (IOException e) {
            System.err.println("Erro ao carregar o template de e-mail: " + e.getMessage());
            // Você pode lançar uma exceção customizada ou seguir sem enviar o e-mail
        }

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

        // Verifica se a senha recebida é diferente da atual
        if (!encoder.matches(dto.password(), client.getPassword())) {
            // Se não bater, significa que é uma nova senha → codifica e atualiza
            client.setPassword(encoder.encode(dto.password()));
        } else {
            // Se for igual, mantém a senha atual
            client.setPassword(client.getPassword());
        }

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