package com.jns.app_manager.utils;

import com.jns.app_manager.security.dtos.UserDetailsDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(final UserDetailsDTO detailsDTO) {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // usa o decodificador padrão
        Key key = Keys.hmacShaKeyFor(keyBytes);
        ;


        return Jwts.builder()
                .claim("id", detailsDTO.getId())
                .claim("name", detailsDTO.getName())
                .claim("authorities", detailsDTO.getAuthorities())
                .claim("sub", detailsDTO.getUsername())
                .signWith(key)
                .claim("exp", new Date(System.currentTimeMillis() + expiration))
                .compact();

    }
}