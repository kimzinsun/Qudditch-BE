package com.goldensnitch.qudditch.service;

import org.springframework.web.multipart.MultipartFile;

public interface OCRService {
    String extractBusinessNumber(MultipartFile file);
}