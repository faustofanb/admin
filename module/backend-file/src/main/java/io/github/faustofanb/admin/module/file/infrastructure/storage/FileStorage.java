package io.github.faustofanb.admin.module.file.infrastructure.storage;

import java.io.InputStream;

public interface FileStorage {
    String upload(InputStream inputStream, String path);

    InputStream download(String path);

    void delete(String path);

    String getType();
}
