package com.jns.app_manager.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "!@#$%&*()-_=+[]{}<>?";
    private static final SecureRandom random = new SecureRandom();

    public static String generate(int length) {
        if (length < 5) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 5 caracteres");
        }

        List<Character> passwordChars = new ArrayList<>();

        // Regras fixas
        passwordChars.add(randomChar(UPPERCASE));         // 1 maiúscula
        passwordChars.add(randomChar(DIGITS));            // 1 número
        passwordChars.add(randomChar(DIGITS));            // 2º número
        passwordChars.add(randomChar(SPECIALS));          // 1 caractere especial

        // Preencher o restante com letras minúsculas
        for (int i = 4; i < length; i++) {
            passwordChars.add(randomChar(LOWERCASE));
        }

        // Embaralhar para evitar padrão previsível
        Collections.shuffle(passwordChars);

        // Montar a senha
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    private static char randomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }
}