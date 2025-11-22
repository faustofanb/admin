package io.github.faustofanb.admin.module.file.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import io.github.faustofanb.admin.module.file.domain.entity.SysFile;

public interface SysFileService {
    SysFile upload(MultipartFile file);

    InputStream download(Long id);

    SysFile getById(Long id);
}
