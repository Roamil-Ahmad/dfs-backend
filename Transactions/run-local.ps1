# Start the transaction service locally on the embedded Tomcat.
#
# application.properties deliberately keeps the three client secrets as
# ${MOBILE_CLIENT_SECRET} / ${POS_CLIENT_SECRET} / ${AGENT_CLIENT_SECRET}
# placeholders so no secret is committed. Spring refuses to start when they are
# unset, which is the "Could not resolve placeholder" failure. This script loads
# them from the git-ignored .env at the repository root and starts the service.
#
# Usage:  .\run-local.ps1

$ErrorActionPreference = 'Stop'

$serviceDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$envFile = Join-Path (Split-Path -Parent $serviceDir) '.env'

if (-not (Test-Path $envFile)) {
    Write-Error "No .env at $envFile - copy .env.example to .env and fill in the values."
}

# Load every KEY=VALUE line into the process environment. Values are never echoed.
Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith('#') -and $line.Contains('=')) {
        $key = $line.Substring(0, $line.IndexOf('=')).Trim()
        $value = $line.Substring($line.IndexOf('=') + 1).Trim()
        if ($key) { Set-Item -Path "env:$key" -Value $value }
    }
}

foreach ($required in @('MOBILE_CLIENT_SECRET', 'POS_CLIENT_SECRET', 'AGENT_CLIENT_SECRET')) {
    if (-not (Get-Item "env:$required" -ErrorAction SilentlyContinue).Value) {
        Write-Error "$required is not set in $envFile"
    }
}

if (-not $env:JAVA_HOME) {
    $env:JAVA_HOME = 'C:\Users\muhammad.kashif\.jdks\ms-17.0.16'
}

Set-Location $serviceDir
Write-Host 'Starting transactions on http://localhost:8009/transactions'
mvn spring-boot:run
