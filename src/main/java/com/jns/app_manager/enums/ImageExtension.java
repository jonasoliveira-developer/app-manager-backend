package com.jns.app_manager.enums;

import lombok.Getter;

import java.util.Arrays;

public enum ImageExtension {
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp");

    private final String extension;
    @Getter
    private final String mimeType;

    ImageExtension(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static ImageExtension fromExtension(String ext) {
        return Arrays.stream(values())
                .filter(e -> e.extension.equalsIgnoreCase(ext))
                .findFirst()
                .orElse(null);
    }

}