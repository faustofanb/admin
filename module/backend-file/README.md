# Backend File Module

This module provides file storage capabilities.

## Configuration

You can configure the storage type using `file.storage.type`.

### Local Storage (Default)

```yaml
file:
  storage:
    type: local
  local:
    path: ./uploads # Default path
```

### MinIO Storage

```yaml
file:
  storage:
    type: minio
  minio:
    endpoint: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    bucket: mybucket
```

## Features

- File Upload
- File Download
- File Deletion
- Metadata storage in database (SysFile)
