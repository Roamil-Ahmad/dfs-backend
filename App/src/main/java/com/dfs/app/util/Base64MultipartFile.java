package com.dfs.app.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64MultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private final String originalFilename;
    private final String contentType;

    public Base64MultipartFile(byte[] fileContent, String originalFilename, String contentType) {
        this.fileContent = fileContent;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException {
        try (InputStream inputStream = getInputStream()) {
            java.nio.file.Files.copy(inputStream, dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static MultipartFile fromBase64(String base64, String fileName, String contentType) {
        if (base64 == null || base64.trim().isEmpty()) {
            throw new CustomException("Failed to convert Base64 string to MultipartFile: no content for file " + fileName);
        }
        try {
            // Strip a data-URL prefix ("data:image/png;base64,....") when the caller sends one.
            // Everything before "base64," is metadata; its characters are themselves valid
            // base64 letters, so leaving it in would decode into leading garbage bytes.
            String base64Content = base64;
            int marker = base64Content.indexOf("base64,");
            if (marker >= 0) {
                base64Content = base64Content.substring(marker + "base64,".length());
            }

            // MIME decoder, not the basic one. Encoders wrap base64 at 76 characters, so the
            // payload arrives with embedded line breaks. Base64.getDecoder() rejects those with
            // "Illegal base64 character a" (0x0a is the newline); getMimeDecoder() skips them,
            // along with any stray spaces or carriage returns.
            byte[] fileBytes = java.util.Base64.getMimeDecoder().decode(base64Content);

            return new Base64MultipartFile(fileBytes, fileName, contentType);
        } catch (Exception e) {
            // Keep the underlying reason: without it the caller only ever sees the generic
            // message and there is nothing to diagnose from.
            throw new CustomException("Failed to convert Base64 string to MultipartFile for file "
                    + fileName + ": " + e.getMessage());
        }
    }
}
