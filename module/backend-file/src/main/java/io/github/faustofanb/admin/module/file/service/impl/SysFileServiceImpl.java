package io.github.faustofanb.admin.module.file.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.faustofanb.admin.module.file.domain.entity.SysFile;
import io.github.faustofanb.admin.module.file.domain.entity.SysFileDraft;
import io.github.faustofanb.admin.module.file.infrastructure.storage.FileStorage;
import io.github.faustofanb.admin.module.file.repository.SysFileRepository;
import io.github.faustofanb.admin.module.file.service.SysFileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysFileServiceImpl implements SysFileService {

    private final SysFileRepository sysFileRepository;
    private final FileStorage fileStorage;

    @Override
    public SysFile upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String fileName = UUID.randomUUID().toString() + extension;

        String path = java.time.LocalDate.now().toString() + "/" + fileName;
        String md5;

        try {
            md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            fileStorage.upload(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }

        SysFile sysFile = SysFileDraft.$.produce(draft -> {
            draft.setFileName(originalFilename);
            draft.setFileType(extension);
            draft.setFileSize(file.getSize());
            draft.setStorageType(fileStorage.getType());
            draft.setFilePath(path);
            draft.setMd5(md5);
            draft.setTenantId(1L); // TODO: get from context
        });

        return sysFileRepository.save(sysFile);
    }

    @Override
    public InputStream download(Long id) {
        SysFile sysFile = sysFileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

        if (!fileStorage.getType().equals(sysFile.storageType())) {
            throw new RuntimeException("Storage mismatch: File is in " + sysFile.storageType()
                    + " but active storage is " + fileStorage.getType());
        }

        return fileStorage.download(sysFile.filePath());
    }

    @Override
    public SysFile getById(Long id) {
        return sysFileRepository.findById(id).orElse(null);
    }
}
