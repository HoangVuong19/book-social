package com.example.file.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.file.config.serialize.ApiResponse;
import com.example.file.service.FileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    @PostMapping("/media/upload")
    ApiResponse<Object> uploadMedia(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(fileService.uploadFile(file));
    }

    @GetMapping("/media/download/{fileName}")
    ResponseEntity<Resource> downloadMedia(@PathVariable String fileName) throws IOException {
        var fileData = fileService.download(fileName);

        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.contentType())
                .body(fileData.resource());
    }
}
