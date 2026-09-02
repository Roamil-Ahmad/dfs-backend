#!/usr/bin/env bash
#
# Start the transaction service locally on the embedded Tomcat.
#
# application.properties deliberately keeps the three client secrets as
# ${MOBILE_CLIENT_SECRET} / ${POS_CLIENT_SECRET} / ${AGENT_CLIENT_SECRET}
# placeholders so no secret is committed. Spring refuses to start when they are
# unset, which is the "Could not resolve placeholder" failure. This script loads
# them from the git-ignored .env at the repository root and starts the service.
#
# Usage:  ./run-local.sh
#
set -euo pipefail

SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$(cd "$SERVICE_DIR/.." && pwd)/.env"

if [ ! -f "$ENV_FILE" ]; then
    echo "No .env at $ENV_FILE - copy .env.example to .env and fill in the values." >&2
    exit 1
fi

# Export everything in .env without echoing any value.
set -a
# shellcheck disable=SC1090
. "$ENV_FILE"
set +a

for required in MOBILE_CLIENT_SECRET POS_CLIENT_SECRET AGENT_CLIENT_SECRET; do
    if [ -z "${!required:-}" ]; then
        echo "$required is not set in $ENV_FILE" >&2
        exit 1
    fi
done

: "${JAVA_HOME:=C:/Users/muhammad.kashif/.jdks/ms-17.0.16}"
export JAVA_HOME

cd "$SERVICE_DIR"
echo "Starting transactions on http://localhost:8009/transactions"
exec mvn spring-boot:run
