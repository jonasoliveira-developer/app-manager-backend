package com.jns.app_manager.service;

import com.jns.app_manager.entity.Person;
import com.jns.app_manager.exceptions.ObjectNotFoundException;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.UserRepository;
import com.jns.app_manager.security.dtos.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return buildUserDetails(user);
        }

        var client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User/Client not found: " + email));
        return buildUserDetails(client);
    }

    private UserDetails buildUserDetails(Person entity) {
        return UserDetailsDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getEmail())
                .password(entity.getPassword())
                .authorities(Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + entity.getAccessLevel().name())))
                .build();
    }
}