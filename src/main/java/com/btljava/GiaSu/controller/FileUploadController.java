package com.btljava.GiaSu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            // Create uploads directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename via UUID to prevent overrides
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return URL (served by WebMvcConfigurer)
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(newFilename)
                    .toUriString();

            response.put("url", fileDownloadUri);
            response.put("fileName", originalFilename);
            
            // Basic guessing of file type implementation
            String contentType = file.getContentType();
            String fileTypeStr = "FILE";
            if (contentType != null && contentType.startsWith("image/")) {
                fileTypeStr = "IMAGE";
            }
            response.put("type", fileTypeStr);

            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("error", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
