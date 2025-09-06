package com.jns.app_manager.service;

import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.UserRepository;
import com.jns.app_manager.utils.ImageData;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public String handleImageUpload(MultipartFile file, String type, UUID id) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem está vazio");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de arquivo inválido. Apenas imagens são permitidas.");
        }

        // Comprimir e redimensionar a imagem
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream)
                .size(600, 600) // Redimensiona para 600x600px
                .outputFormat("jpg") // Converte para JPEG
                .outputQuality(0.6f) // Qualidade de compressão (60%)
                .toOutputStream(outputStream);

        byte[] compressedBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(compressedBytes);

        // Verifica se cabe no campo TEXT (~65.535 bytes)
        if (base64Image.length() > 65000) {
            throw new IllegalArgumentException("Imagem comprimida ainda excede o limite do banco.");
        }

        switch (type.toLowerCase()) {
            case "user" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageProfile(base64Image);
                user.setImageMimeType("image/jpeg");
                userRepository.save(user);
            }
            case "client" -> {
                var client = clientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                client.setImageProfile(base64Image);
                client.setImageMimeType("image/jpeg");
                clientRepository.save(client);
            }
            case "watermark" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageWatermark(base64Image);
                user.setImageMimeType("image/jpeg");
                userRepository.save(user);
            }
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client' ou 'watermark'.");
        }

        return "Imagem salva com sucesso";
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
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client' ou 'watermark'.");
        };
    }
}
