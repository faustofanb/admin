package io.github.faustofanb.admin.module.file.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.faustofanb.admin.common.model.Result;
import io.github.faustofanb.admin.module.file.domain.entity.SysFile;
import io.github.faustofanb.admin.module.file.service.SysFileService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class SysFileController {

    private final SysFileService sysFileService;

    @PostMapping("/upload")
    public Result<SysFile> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(sysFileService.upload(file));
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam Long id) {
        SysFile sysFile = sysFileService.getById(id);
        InputStream inputStream = sysFileService.download(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(sysFile.fileName(), StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }
}
