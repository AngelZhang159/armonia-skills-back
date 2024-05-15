package com.armoniacode.armoniaskills.controller;

import com.armoniacode.armoniaskills.entity.Image;
import com.armoniacode.armoniaskills.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/file")
@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        Image fileEntity = fileStorageService.storeFile(file, file.getContentType());
        String downloadUri = "/api/v1/file/download/" + fileEntity.getId();
        return ResponseEntity.created(new URI(downloadUri)).body(downloadUri);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID fileId) {
        Optional<Image> fileEntityOptional = fileStorageService.getFile(fileId);

        if (fileEntityOptional.isPresent()) {
            Image fileEntity = fileEntityOptional.get();
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getImageData());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileEntity.getFileName())
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }
}