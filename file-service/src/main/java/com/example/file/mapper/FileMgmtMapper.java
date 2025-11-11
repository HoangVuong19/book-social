package com.example.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.file.config.serialize.FileInfo;
import com.example.file.entity.FileMgmt;

@Mapper(componentModel = "spring")
public interface FileMgmtMapper {
    @Mapping(target = "id", source = "name")
    FileMgmt toFileMgmt(FileInfo fileInfo);
}
