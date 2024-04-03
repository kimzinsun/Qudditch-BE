package com.goldensnitch.qudditch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    // private final Path rootLocation = Paths.get("classpath:/static/uploads");
    private final Path rootLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            Path destinationFile = rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile);
            return destinationFile.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}