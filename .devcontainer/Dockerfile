FROM mcr.microsoft.com/devcontainers/java:21

# Install Node.js (for frontend)
ARG NODE_VERSION="20"
RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && nvm install ${NODE_VERSION} 2>&1"

# Install additional OS packages if needed
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>
