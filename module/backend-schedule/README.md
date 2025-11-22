# Backend Schedule Module

This module integrates PowerJob for distributed task scheduling.

## Configuration

Add the following configuration to your `application.yml`:

```yaml
powerjob:
  worker:
    enabled: true
    app-name: admin-backend
    server-address: 127.0.0.1:7700
    store-strategy: disk
    protocol: http
```

## Processors

### LogCleanupProcessor

Cleans up operation logs older than a specified number of days.

- **Job Params**: Number of days to keep (default: 30).
