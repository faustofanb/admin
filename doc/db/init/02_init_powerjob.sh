#!/bin/bash
set -e

echo "Initializing PowerJob database..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "powerjob_product" -f /workspaces/admin/doc/db/powerjob/powerjob_postgres.sql
