package com.jns.app_manager.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.jns.app_manager.enums.ImageExtension;
import com.jns.app_manager.repository.ClientRepository;
import com.jns.app_manager.repository.ReportRepository;
import com.jns.app_manager.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ReportRepository reportRepository;

    @Value("${stackhero.s3.access-key}")
    private String accessKey;

    @Value("${stackhero.s3.secret-key}")
    private String secretKey;

    @Value("${stackhero.s3.bucket}")
    private String bucketName;

    @Value("${stackhero.s3.endpoint}")
    private String endpoint;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration config = new ClientConfiguration();
        config.setSignerOverride("AWSS3V4SignerType");

        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "auto"))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String handleImageUpload(MultipartFile file, String type, UUID id, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem está vazio");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Arquivo sem extensão");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        ImageExtension imageExt = ImageExtension.fromExtension(ext);
        if (imageExt == null) {
            throw new IllegalArgumentException("Extensão não permitida: " + ext);
        }

        // Comprimir e redimensionar
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(600, 600)
                .outputFormat("jpg")
                .outputQuality(0.6f)
                .toOutputStream(outputStream);

        byte[] compressedBytes = outputStream.toByteArray();

        if (compressedBytes.length > 65000) {
            throw new IllegalArgumentException("Imagem comprimida ainda excede o limite permitido.");
        }

        // Gerar nome único
        String filename = "images/" + UUID.randomUUID() + ".jpg";

        // Upload para Stackhero S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
        metadata.setContentLength(compressedBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, filename, inputStream, metadata);
        s3Client.putObject(putRequest);

        // URL pública
        String imageUrl = String.format("https://%s.s3.stackhero.io/%s", bucketName, filename);

        // Atualizar entidade no banco
        switch (type.toLowerCase()) {
            case "user" -> {
                var user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                user.setImageUrl(imageUrl);
                user.setImageMimeType("image/jpeg");
                userRepository.save(user);
            }
            case "client" -> {
                var client = clientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                client.setImageUrl(imageUrl);
                client.setImageMimeType("image/jpeg");
                clientRepository.save(client);
            }
//            case "watermark" -> {
//                var user = userRepository.findById(id)
//                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//                user.set(imageUrl);
//                user.setImageMimeType("image/jpeg");
//                userRepository.save(user);
//            }
            case "assignclient" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignUrlClient(imageUrl);
                report.setAssignClientMimeType("image/jpeg");
                reportRepository.save(report);
            }
            case "assignuser" -> {
                var report = reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Relatório não encontrado"));
                report.setAssignUrlUser(imageUrl);
                report.setAssignUserMimeType("image/jpeg");
                reportRepository.save(report);
            }
            default -> throw new IllegalArgumentException("Tipo inválido. Use 'user', 'client', 'watermark', 'assignClient' ou 'assignUser'.");
        }

        return imageUrl;
    }
}