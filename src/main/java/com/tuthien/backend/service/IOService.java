package com.tuthien.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class IOService {

    @Value("${spring.image.store.url}")
    private String imageStoreUrl;

    public Optional<String> saveImageToStore(MultipartFile file) {
        try {
            File f = new File(this.imageStoreUrl  + file.getOriginalFilename());
            file.transferTo(f);
            return Optional.of(file.getOriginalFilename());
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return Optional.empty();
        }
    }
}
