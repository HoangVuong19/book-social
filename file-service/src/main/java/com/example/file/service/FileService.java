package com.example.file.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.file.dto.response.FileData;
import com.example.file.dto.response.FileResponse;
import com.example.file.exception.NotFoundException;
import com.example.file.mapper.FileMgmtMapper;
import com.example.file.repository.FileMgmtRepository;
import com.example.file.repository.FileRepository;
import com.example.file.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    FileRepository fileRepository;
    FileMgmtRepository fileMgmtRepository;
    FileMgmtMapper fileMgmtMapper;
    UserContext userContext;

    public FileResponse uploadFile(MultipartFile file) throws IOException {
        // Store file
        var fileInfo = fileRepository.store(file);

        // Create file management info
        var fileMgmt = fileMgmtMapper.toFileMgmt(fileInfo);
        String userId = userContext.getCurrentUsername();
        fileMgmt.setOwnerId(userId);
        fileMgmtRepository.save(fileMgmt);
        return new FileResponse(file.getOriginalFilename(), fileInfo.getUrl());
    }

    public FileData download(String fileName) throws IOException {
        var fileMgmt = fileMgmtRepository.findById(fileName).orElseThrow(() -> new NotFoundException("File not found"));

        var resource = fileRepository.read(fileMgmt);

        return new FileData(fileMgmt.getContentType(), resource);
    }
}
