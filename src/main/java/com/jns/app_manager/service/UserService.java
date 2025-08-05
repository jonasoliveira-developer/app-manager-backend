package com.jns.app_manager.service;

import com.jns.app_manager.dtos.UserRequestDTO;
import com.jns.app_manager.dtos.UserResponseDTO;
import com.jns.app_manager.dtos.mapper.UserMapper;
import com.jns.app_manager.entity.User;
import com.jns.app_manager.enums.AccessLevel;
import com.jns.app_manager.enums.AccountStatus;
import com.jns.app_manager.enums.SubscriptionType;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;


    public UserResponseDTO findById(UUID id) {
        var user = findUser(id);
        return mapper.toResponse(user);
    }

    public UserResponseDTO save(UserRequestDTO dto) {
        var user = mapper.toEntity(dto);

        user.setPassword(encoder.encode(dto.password()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccessLevel(AccessLevel.USER);

        return mapper.toResponse(userRepository.save(user));
    }

    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        var user = findUser(id);

        user.setName(dto.name());
        user.setPhoneNumber(dto.phoneNumber());
        user.setCouncilRegistrationNumber(dto.councilRegistrationNumber());
        user.setSubscriptionType(SubscriptionType.valueOf(dto.subscriptionType()));

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

}