package com.jns.app_manager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendTemplatedEmail(String to, String subject, String body, String userName, String password) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("appmanageradm@gmail.com");


        String htmlBody = buildHtmlTemplate(subject, body, userName, password, "https://app-manager-eight.vercel.app/login");
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    public String buildHtmlTemplate(String title, String messageBody, String userName, String password, String siteUrl) {
        String passwordSection = "";

        if (password != null && !password.isBlank()) {
            passwordSection = """
                        <div style="text-align: center; margin: 40px 0;">
                            <p style="font-size: 18px; color: #00A859; font-weight: bold;">Sua senha de acesso:</p>
                            <div style="font-size: 24px; font-weight: bold; color: #000000; background-color: #f0f0f0; padding: 15px 25px; border-radius: 6px; display: inline-block;">
                                %s
                            </div>
                        </div>
                    """.formatted(password);
        }

        return """
                    <html>
                        <body style="margin: 0; padding: 0; background-color: #1a1a1a; font-family: Arial, sans-serif;">
                            <div style="background-color: #00A859; padding: 20px; text-align: center;">
                                <h1 style="font-size: 32px; margin: 0; font-weight: bold;">
                                    <span style="color: #ffffff;">Fisio</span><span style="color: #000000;">Admin</span>
                                </h1>
                            </div>
                
                            <div style="max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0,0,0,0.2);">
                                <div style="padding: 30px;">
                                    <h2 style="color: #333333; font-size: 20px; margin-bottom: 10px;"><strong>%s</strong></h2>
                                    <p style="color: #555555; font-size: 16px; margin-bottom: 30px;">%s</p>
                                    %s
                                    <p style="color: #888888; font-size: 14px;">Atenciosamente,<br><strong>%s</strong></p>
                                </div>
                
                                <div style="text-align: center; padding: 20px; background-color: #f9f9f9;">
                                    <a href="%s" style="display: inline-block; background-color: #00A859; color: #ffffff; font-weight: bold; text-decoration: none; padding: 12px 24px; border-radius: 6px;">
                                        Acessar o site
                                    </a>
                                </div>
                            </div>
                        </body>
                    </html>
                """.formatted(title, messageBody, passwordSection, userName, siteUrl);
    }
}