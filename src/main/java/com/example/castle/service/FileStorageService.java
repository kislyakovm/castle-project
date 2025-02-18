package com.example.castle.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    public String storeFile(MultipartFile file, String directory, String filename) {
        // Здесь будет логика сохранения файла
        // Пока возвращаем заглушку
        return "/uploads/" + directory + "/" + filename + "_" + System.currentTimeMillis();
    }
}