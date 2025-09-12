package com.jns.app_manager.controller;

import com.jns.app_manager.dtos.EmailRequestDTO;
import com.jns.app_manager.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO request) {
        try {
            emailService.sendTemplatedEmail(
                    request.getTo(),
                    request.getSubject(),
                    request.getBody(),
                    request.getUserName(),
                    request.getPassword() // pode ser null ou vazio
            );
            return ResponseEntity.ok("E-mail enviado com sucesso!");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}