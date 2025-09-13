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

        // Estratégia de compressão adaptativa
        byte[] compressedBytes = null;
        int size = 300;
        float quality = 1.0f;

        while (size >= 100 && quality >= 0.4f) {
            InputStream inputStream = file.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(size, size)
                    .outputFormat("jpg") // JPEG com compressão ajustável
                    .outputQuality(quality)
                    .toOutputStream(outputStream);

            compressedBytes = outputStream.toByteArray();

            if (compressedBytes.length <= 65000) {
                break;
            }

            // Reduz progressivamente
            size -= 50;
            quality -= 0.1f;
        }

        if (compressedBytes.length > 65000) {
            throw new IllegalArgumentException("Não foi possível comprimir a imagem dentro do limite de 65KB.");
        }

        String baseUri = request.getRequestURL().toString()
                .replace(request.getRequestURI(), request.getContextPath());

        String imageUrl = String.format("%s/images/view?type=%s&id=%s", baseUri, type.toLowerCase(), id);

        switch (type.toLowerCase()) {
            case "user" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageProfile(compressedBytes);
                user.setImageMimeType("image/jpeg");
                user.setImageUrl(imageUrl);
                userRepository.save(user);
            }
            case "client" -> {
                var client = clientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                client.setImageProfile(compressedBytes);
                client.setImageMimeType("image/jpeg");
                client.setImageUrl(imageUrl);
                clientRepository.save(client);
            }
            case "watermark" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageWatermark(compressedBytes);
                user.setImageMimeType("image/jpeg");
                user.setImageUrl(imageUrl);
                userRepository.save(user);
            }
            case "assignclient" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignClient(compressedBytes);
                report.setAssignClientMimeType("image/jpeg");
                report.setAssignUrlClient(imageUrl);
                reportRepository.save(report);
            }
            case "assignuser" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignUser(compressedBytes);
                report.setAssignUserMimeType("image/jpeg");
                report.setAssignUrlUser(imageUrl);
                reportRepository.save(report);
            }
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client', 'watermark', 'assignClient' ou 'assignUser'.");
        }

        return imageUrl;
    }
}
