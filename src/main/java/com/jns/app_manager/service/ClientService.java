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
        var clients = clientRepository.findByUserIdAndNameContainingIgnoreCaseAndAccountStatusNot(
                userId,
                nameVerified,
                AccountStatus.DELETED,
                pageable
        );
        return clients.map(mapper::toResponse);
    }

    public ClientResponseDTO save(ClientRequestDTO dto) {
        var user = userService.findUser(dto.userId());

        var client = mapper.toEntity(dto);

        // Gera senha personalizada: 8 caracteres
        var basePassword = PasswordGenerator.generate(8);

        // Extrai o primeiro nome do usuário
        var firstName = dto.name().split(" ")[0].toLowerCase();

        // Usa o número do conselho ou valor padrão
        var councilSuffix = user.getCouncilRegistrationNumber() != null ? user.getCouncilRegistrationNumber() : "000000";

        // Monta o e-mail: primeiroNome@registro.com
        var email = firstName + "@" + councilSuffix + ".com";

        client.setPassword(encoder.encode(basePassword));
        client.setAccessLevel(AccessLevel.CLIENT);
        client.setAccountStatus(AccountStatus.ACTIVE);
        client.setEmail(email);
        client.setUser(user);

        var savedClient = clientRepository.save(client);

        // Envia e-mail com as credenciais
        String bodyEmailMessage = String.format(
                "Olá <strong>%s</strong>,<br><br>" +
                        "É um prazer recebê-lo(a) no <strong>FisioAdmin</strong>, sua nova plataforma de gestão inteligente para profissionais da saúde.<br><br>" +
                        "Abaixo estão suas credenciais de acesso. Por segurança, recomendamos que você acesse o aplicativo e personalize sua senha diretamente no seu perfil.<br><br>" +
                        "<strong>E-mail de acesso:</strong> %s<br>" +
                        "<strong>Senha provisória:</strong> %s<br><br>" +
                        "Estamos felizes em tê-lo(a) conosco e desejamos uma excelente jornada!",
                user.getName(),
                email,
                basePassword
        );
        try {
            emailService.sendTemplatedEmail(
                    user.getEmail(),
                    "Credenciais de acesso",
                    bodyEmailMessage,
                    "FisioAdmin",
                    basePassword
            );
        } catch (IOException e) {
            // Loga o erro e continua o fluxo
            System.err.println("Erro ao carregar o template de e-mail: " + e.getMessage());
            // Se quiser, pode lançar uma exceção customizada ou apenas seguir sem enviar o e-mail
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return mapper.toResponse(savedClient);
    }
    public ClientResponseDTO update(UUID id, ClientRequestDTO dto) {
        var client = findClient(id);

        client.setName(dto.name());
        client.setPhoneNumber(dto.phoneNumber());
        client.setAge(dto.age());
        client.setWeight(dto.weight());
        client.setHeight(dto.height());
        client.setLocal(dto.local());

        // Verifica se o e-mail recebido é diferente do atual
        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equals(client.getEmail())) {
            client.setEmail(dto.email());
        }

        // Verifica se a senha recebida é diferente da atual
        if (dto.password() != null && !dto.password().isBlank() && !encoder.matches(dto.password(), client.getPassword())) {
            client.setPassword(encoder.encode(dto.password()));
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