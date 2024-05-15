package com.armoniacode.armoniaskills.service;

import com.armoniacode.armoniaskills.entity.Image;
import com.armoniacode.armoniaskills.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private FileRepository fileRepository;

    public Image storeFile(MultipartFile file, String contentType) throws IOException {
        Image fileEntity = new Image();
        fileEntity.setContentType(contentType);
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setImageData(file.getBytes());

        return fileRepository.save(fileEntity);
    }

    public Optional<Image> getFile(UUID fileId) {
        return fileRepository.findById(fileId);
    }

}