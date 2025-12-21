package io.github.faustofan.admin.shared.web.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "app.http-log")
public class HttpLogProperties {

    private boolean enabled = true;
    private boolean maskEnabled = true;
    private int minimumStatus = 0;
    private int maxBodySize = 8192;

    private Set<String> includePaths = new HashSet<>(Set.of("/api/**"));

    private Set<String> excludePaths = new HashSet<>(Set.of(
            "/actuator/**",
            "/openapi.yml",
            "/ts.zip",
            "/doc.html",
            "/assets/**",
            "/favicon.ico"
    ));

    private Set<String> sensitiveHeaders = new HashSet<>(Set.of(
            "authorization", "x-auth-token", "x-api-key", "cookie", "set-cookie"
    ));

    private Set<String> sensitiveFields = new HashSet<>(Set.of(
            "password", "passwordHash", "newPassword", "oldPassword", "accessToken",
            "refreshToken", "token", "secret", "apiKey", "secretKey"
    ));

    // Getters and Setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isMaskEnabled() {
        return maskEnabled;
    }

    public void setMaskEnabled(boolean maskEnabled) {
        this.maskEnabled = maskEnabled;
    }

    public int getMinimumStatus() {
        return minimumStatus;
    }

    public void setMinimumStatus(int minimumStatus) {
        this.minimumStatus = minimumStatus;
    }

    public int getMaxBodySize() {
        return maxBodySize;
    }

    public void setMaxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    public Set<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(Set<String> includePaths) {
        this.includePaths = includePaths;
    }

    public Set<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(Set<String> excludePaths) {
        this.excludePaths = excludePaths;
    }

    public Set<String> getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    public void setSensitiveHeaders(Set<String> sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public Set<String> getSensitiveFields() {
        return sensitiveFields;
    }

    public void setSensitiveFields(Set<String> sensitiveFields) {
        this.sensitiveFields = sensitiveFields;
    }
}