package com.jns.app_manager.security.dtos;

import com.jns.app_manager.dtos.AuthenticateRequest;
import com.jns.app_manager.dtos.AuthenticateResponse;
import com.jns.app_manager.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Log4j2
@RequiredArgsConstructor
public class JWTAuthenticationImpl {
 private final AuthenticationManager authenticationManager;
 private final JWTUtils jwtUtils;
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        try {
            log.info("Authenticate User: {}", request.email());
            var authResult = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            return buildAuthenticationResponse((UserDetailsDTO) authResult.getPrincipal());
        } catch (BadCredentialsException ex) {
            log.error("Error on authenticate user: {} ", ex.getMessage());
            throw new BadCredentialsException("Bad Credentials");
        }

    }

    protected AuthenticateResponse buildAuthenticationResponse(UserDetailsDTO detailsDTO) {
        log.info("Successfully authenticated User: {}", detailsDTO.getUsername());
        var token = jwtUtils.generateToken(detailsDTO);
        return AuthenticateResponse.builder()
                .type("JWT")
                .token("Bearer " + token)
                .build();
    }
}
