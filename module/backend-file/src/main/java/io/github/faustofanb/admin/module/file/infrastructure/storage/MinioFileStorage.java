package io.github.faustofanb.admin.module.file.infrastructure.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;

@Component
@ConditionalOnProperty(name = "file.storage.type", havingValue = "minio")
public class MinioFileStorage implements FileStorage {

    @Value("${file.minio.endpoint}")
    private String endpoint;

    @Value("${file.minio.access-key}")
    private String accessKey;

    @Value("${file.minio.secret-key}")
    private String secretKey;

    @Value("${file.minio.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .stream(inputStream, -1, 10485760)
                            .build());
            return path;
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload failed", e);
        }
    }

    @Override
    public InputStream download(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO download failed", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO delete failed", e);
        }
    }

    @Override
    public String getType() {
        return "MINIO";
    }
}
