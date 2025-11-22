package io.github.faustofanb.admin.module.file.infrastructure.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "file.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorage implements FileStorage {

    @Value("${file.local.path:./uploads}")
    private String basePath;

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            Path targetPath = Paths.get(basePath, path);
            Files.createDirectories(targetPath.getParent());
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public InputStream download(String path) {
        try {
            Path targetPath = Paths.get(basePath, path);
            return new FileInputStream(targetPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Path targetPath = Paths.get(basePath, path);
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            throw new RuntimeException("File delete failed", e);
        }
    }

    @Override
    public String getType() {
        return "LOCAL";
    }
}
