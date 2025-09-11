package com.jns.app_manager.controller;

import com.jns.app_manager.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam("id") UUID id,
            HttpServletRequest request
    ) {
        try {
            // O método agora retorna a URL completa da imagem
            String imageUrl = imageService.handleImageUpload(file, type, id, request);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagem salva com sucesso",
                    "url", imageUrl
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Falha ao salvar imagem"));
        }
    }

    @GetMapping("/view")
    public ResponseEntity<byte[]> viewImage(
            @RequestParam("type") String type,
            @RequestParam("id") UUID id
    ) {
        try {
            var imageData = imageService.getImageData(type, id);
            MediaType mediaType = MediaType.parseMediaType(imageData.mimeType());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            return new ResponseEntity<>(imageData.bytes(), headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}