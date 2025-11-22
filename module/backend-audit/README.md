# Backend Audit Module

This module provides audit logging and monitoring capabilities.

## Features

- **Operation Log**: Records user operations via `@Log` annotation.
- **Login Log**: Records user login attempts (to be integrated with Auth module).
- **Monitoring**: APIs to query and clean logs.

## Usage

### @Log Annotation

Annotate your controller methods with `@Log` to record operations.

```java
@Log(title = "User Management", businessType = BusinessType.INSERT)
@PostMapping
public Result<Void> add(@RequestBody UserDTO user) {
    // ...
}
```

### Configuration

Ensure `backend-audit` is included in your boot application dependencies.

## Entities

- `SysOperLog`: Operation logs.
- `SysLogininfor`: Login logs.
