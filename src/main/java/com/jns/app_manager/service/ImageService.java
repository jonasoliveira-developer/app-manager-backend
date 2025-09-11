package com.jns.app_manager.service;

import com.jns.app_manager.enums.ImageExtension;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.ReportRepository;
import com.jns.app_manager.repository.UserRepository;
import com.jns.app_manager.utils.ImageData;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ReportRepository reportRepository;

    public String handleImageUpload(MultipartFile file, String type, UUID id, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem está vazio");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Arquivo sem extensão");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        ImageExtension imageExt = ImageExtension.fromExtension(ext);
        if (imageExt == null) {
            throw new IllegalArgumentException("Extensão não permitida: " + ext);
        }

        // Comprimir e redimensionar a imagem
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream)
                .size(600, 600)
                .outputFormat("jpg")
                .outputQuality(0.6f)
                .toOutputStream(outputStream);

        byte[] compressedBytes = outputStream.toByteArray();

        if (compressedBytes.length > 65000) {
            throw new IllegalArgumentException("Imagem comprimida ainda excede o limite do banco.");
        }

        String baseUri = request.getRequestURL().toString()
                .replace(request.getRequestURI(), request.getContextPath());

        String imageUrl = String.format("%s/images/view?type=%s&id=%s", baseUri, type.toLowerCase(), id);

        switch (type.toLowerCase()) {
            case "user" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageProfile(compressedBytes);
                user.setImageMimeType(file.getContentType());
                user.setImageUrl(imageUrl);
                userRepository.save(user);
            }
            case "client" -> {
                var client = clientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                client.setImageProfile(compressedBytes);
                client.setImageMimeType(file.getContentType());
                client.setImageUrl(imageUrl);
                clientRepository.save(client);
            }
            case "watermark" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageWatermark(compressedBytes);
                user.setImageMimeType(file.getContentType());
                user.setImageUrl(imageUrl);
                userRepository.save(user);
            }
            case "assignclient" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignClient(compressedBytes);
                report.setAssignClientMimeType(file.getContentType());
                report.setAssignUrlClient(imageUrl);
                reportRepository.save(report);
            }
            case "assignuser" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignUser(compressedBytes);
                report.setAssignUserMimeType(file.getContentType());
                report.setAssignUrlUser(imageUrl);
                reportRepository.save(report);
            }
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client', 'watermark', 'assignClient' ou 'assignUser'.");
        }

        return imageUrl;
    }

    public ImageData getImageData(String type, UUID id) {
        return switch (type.toLowerCase()) {
            case "user" -> userRepository.findById(id)
                    .map(user -> new ImageData(user.getImageProfile(), user.getImageMimeType()))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            case "client" -> clientRepository.findById(id)
                    .map(client -> new ImageData(client.getImageProfile(), client.getImageMimeType()))
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            case "watermark" -> userRepository.findById(id)
                    .map(user -> new ImageData(user.getImageWatermark(), user.getImageMimeType()))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            case "assignclient" -> reportRepository.findById(id)
                    .map(report -> new ImageData(report.getAssignClient(), report.getAssignClientMimeType()))
                    .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
            case "assignuser" -> reportRepository.findById(id)
                    .map(report -> new ImageData(report.getAssignUser(), report.getAssignUserMimeType()))
                    .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client', 'watermark', 'assignClient' ou 'assignUser'.");
        };
    }
}