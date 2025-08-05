package com.jns.app_manager.controller;

import com.jns.app_manager.dtos.AuthenticateRequest;
import com.jns.app_manager.dtos.AuthenticateResponse;
import com.jns.app_manager.security.dtos.JWTAuthenticationImpl;
import com.jns.app_manager.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtils jwtUtils;
    private final AuthenticationConfiguration authenticationConfiguration;

    @PostMapping("/login")
    ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(authenticationConfiguration.getAuthenticationManager(), jwtUtils)
                        .authenticate(request)
        );
    }
}
