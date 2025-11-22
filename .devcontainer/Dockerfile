FROM mcr.microsoft.com/devcontainers/java:21

# Install Docker CLI manually to avoid feature build failures
RUN apt-get update && \
    apt-get install -y docker.io btop maven && \
    apt-get clean -y && rm -rf /var/lib/apt/lists/*
