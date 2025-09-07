package com.jns.app_manager.service;

import com.jns.app_manager.dtos.UserRequestDTO;
import com.jns.app_manager.dtos.UserResponseDTO;
import com.jns.app_manager.dtos.mapper.UserMapper;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import com.jns.app_manager.enums.SubscriptionType;
import com.jns.app_manager.exceptions.EmailAlreadyExistsException;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.UserRepository;
import com.jns.app_manager.utils.PasswordGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;


    public UserResponseDTO findById(UUID id) {
        var user = findUser(id);
        return mapper.toResponse(user);
    }

    public UserResponseDTO save(UserRequestDTO dto) throws MessagingException {
        validateEmailUniqueness(dto.email());

        var user = mapper.toEntity(dto);
        var password = PasswordGenerator.generate(8);
        user.setPassword(encoder.encode(password));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccessLevel(AccessLevel.USER);
        user.setSubscriptionType(SubscriptionType.FREE);

        String bodyEmailMessage = String.format(
                "Olá <strong>%s</strong>,<br><br>" +
                        "É um prazer recebê-lo(a) no <strong>FisioAdmin</strong>, sua nova plataforma de gestão inteligente para profissionais da saúde.<br><br>" +
                        "Abaixo estão suas credenciais de acesso. Por segurança, recomendamos que você acesse o aplicativo e personalize sua senha diretamente no seu perfil.<br><br>" +
                        "Estamos felizes em tê-lo(a) conosco e desejamos uma excelente jornada!",
                user.getName()
        );

        try {
            emailService.sendTemplatedEmail(
                    user.getEmail(),
                    "Credenciais de acesso",
                    bodyEmailMessage,
                    "FisioAdmin",
                    password
            );
        } catch (IOException e) {
            // Loga o erro e continua o fluxo
            System.err.println("Erro ao carregar o template de e-mail: " + e.getMessage());
            // Se quiser, pode lançar uma exceção customizada ou apenas seguir sem enviar o e-mail
        }

        return mapper.toResponse(userRepository.save(user));
    }

    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        var user = findUser(id);

        user.setName(dto.name());
        user.setPhoneNumber(dto.phoneNumber());
        user.setCouncilRegistrationNumber(dto.councilRegistrationNumber());

        if (dto.subscriptionType() != null && !dto.subscriptionType().isBlank()) {
            user.setSubscriptionType(SubscriptionType.valueOf(dto.subscriptionType()));
        }

        String novaSenha = dto.password();
        if (novaSenha != null && !novaSenha.isBlank()) {
            if (!encoder.matches(novaSenha, user.getPassword())) {
                user.setPassword(encoder.encode(novaSenha));
            }
            // Se for igual, não precisa fazer nada — evita reatribuição desnecessária
        }

        return mapper.toResponse(userRepository.save(user));
    }
    public void delete(UUID id) {
        var user = findUser(id);

        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    public User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado: " + id));
    }

    private void validateEmailUniqueness(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new EmailAlreadyExistsException(email);
        });
    }

}