# DFS Pakistan — Backend Platform

Digital Financial Services platform for Pakistan: **13 Spring Boot services**,
orchestrated with Docker Compose and backed by an **external Oracle database**.

---

## Contents

- [Architecture](#architecture)
- [Service inventory](#service-inventory)
- [Prerequisites](#prerequisites)
- [Quick start (Docker)](#quick-start-docker)
- [Running locally without Docker](#running-locally-without-docker)
- [Configuration](#configuration)
- [Service URLs](#service-urls)
- [Ports](#ports)
- [Database](#database)
- [Secrets](#secrets)
- [Building](#building)
- [Troubleshooting](#troubleshooting)
- [Repository layout](#repository-layout)

---

## Architecture

Every service is an independent Spring Boot 2.7 application on **Java 17**, packaged as a WAR and
run by the embedded container. Services talk to each other over plain HTTP and share a single
Oracle schema.

```
                        +--------------+
   mobile apps -------->|   gateway    |  AES payload encryption/decryption
                        +------+-------+
                               |
  +------------+--------------+-----------------+
  v            v               v                v
agentapp      app         transactions      backoffice
  |            |               |                |
  |            |               +--> switch-gateway --> switch-simulator
  |            |                        (ISO 8583 / 1LINK)
  +------------+--> thirdparties     (OTP, notifications, geocoding)
  +---------------> nadra            (CNIC verification)
  +---------------> workflow         (maker-checker)
  +---------------> fee              (pricing and commission)
  +---------------> cardwrapper      (card management)
  +---------------> pushnotification (Firebase)

                    v
            Oracle  (external - NOT part of this compose project)
```

`switch-simulator` stands in for the 1LINK switch during development. It is not a production
component; leave it out of real deployments.

---

## Service inventory

| Service | Directory | Context path | Container port | Host port | Purpose |
| --- | --- | --- | --- | --- | --- |
| agentapp | `AgentApp/` | `/agentapp` | 8001 | 18001 | Agent mobile app backend, agent onboarding and KYC |
| app | `App/` | `/app` | 8002 | 18002 | Customer mobile app backend, customer KYC |
| backoffice | `backoffice/` | `/backoffice` | 8003 | 18003 | Admin portal backend; serves uploaded KYC documents |
| cardwrapper | `CardManagement/` | `/cardwrapper` | 8005 | 18005 | Card issuance and management |
| fee | `pricingandcommission/` | `/fee` | 8007 | 18007 | Pricing, fees and commission calculation |
| thirdparties | `ThirdParties/` | `/thirdparties` | 8008 | 18008 | OTP, notifications, reverse geocoding |
| transactions | `Transactions/` | `/transactions` | 8009 | 18009 | Funds transfer, IBFT, bill payment, wallet operations |
| workflow | `Workflow/` | `/workflow` | 8010 | 18010 | Maker-checker approval workflow |
| pushnotification | `pushnotification/` | `/firbase` | 8012 | 18012 | Firebase Cloud Messaging |
| gateway | `apigatway-encryption/` | `/dfs-crypto-gateway` | 8080 | 18013 | AES request/response encryption gateway |
| nadra | `nadra/` | `/nadra` | 9994 | 18014 | NADRA CNIC verification |
| switch-gateway | `switch-gateway/` | `/switch-gateway` | 9995 | 18015 | ISO 8583 / 1LINK switch adapter |
| switch-simulator | `switch-simulator/` | `/switch-simulator` | 9993 | 18016 | 1LINK switch simulator (development only) |

The `/firbase` context path is spelled that way in production clients. It is a typo that has been
in the contract long enough to be load-bearing — do not "fix" it.

---

## Prerequisites

| | Version | Needed for |
| --- | --- | --- |
| Java (JDK) | **17** | building and running locally |
| Maven | 3.8+ | building |
| Docker Engine | 20.10+ | container builds |
| Docker Compose | v2 (`docker compose`) | orchestration |
| Oracle | 19c, reachable over the network | all services |

You do **not** need Java or Maven installed to run via Docker — the Dockerfiles build inside a
`maven:3.9.9-eclipse-temurin-17` stage and ship only the JRE.

---

## Quick start (Docker)

```bash
git clone https://github.com/Roamil-Ahmad/dfs-backend.git
cd dfs-backend

cp .env.example .env
#  edit .env - at minimum DB_URL, DB_USERNAME, DB_PASSWORD and the secrets block

docker compose build          # first build pulls Maven dependencies, expect 10-20 min
docker compose up -d
docker compose ps
```

The React admin portal is **not** part of this repository. It is a prebuilt static bundle with
backend URLs compiled into it, deployed separately behind its own web server; it calls
backoffice, fee and workflow directly from the browser.

Useful commands:

```bash
docker compose logs -f transactions        # follow one service
docker compose up -d --build transactions  # rebuild and restart one service
docker compose down                        # stop everything (containers only)
docker compose config                      # validate compose + .env substitution
```

To skip the switch simulator, or any other service, just name the ones you want:

```bash
docker compose up -d agentapp app transactions backoffice
```

---

## Running locally without Docker

Each service is a standalone Spring Boot application and can be run on its own.

```bash
export DB_URL=jdbc:oracle:thin:@your-oracle-host:1521/ORCLPDB1
export DB_USERNAME=your_schema
export DB_PASSWORD=your_password

cd Transactions
mvn spring-boot:run
```

The service then answers on its **container port**, not the host port — `Transactions` listens on
<http://localhost:8009/transactions>.

One thing to be aware of: the inter-service URL defaults compiled into `application.properties` are
the **Docker** addresses (`http://thirdparties:8008`, and so on), because that is what Compose
needs. Those names do not resolve outside the Docker network. If a service you run locally calls
another service, export the local overrides too — the commented block at the bottom of
`.env.example` lists every one of them:

```bash
export THIRDPARTIES_BASE_URL=http://localhost:18008
export TRANSACTIONS_BASE_URL=http://localhost:18009
```

To run two copies of a service side by side, override the listen port:

```bash
SERVER_PORT=18109 mvn spring-boot:run
```

---

## Configuration

All configuration is supplied through environment variables. Nothing needs to be edited inside
`application.properties` to deploy, and no credential is compiled into the build.

- `.env.example` — the template, committed, contains **placeholders only**
- `.env` — your real values, **git-ignored**, never committed

Compose feeds `.env` into every container via `env_file`. For local runs, export the same
variables in your shell.

Variables fall into four groups; see `.env.example` for the annotated list.

| Group | Behaviour when unset |
| --- | --- |
| `DB_*` | **Startup fails** — `Could not resolve placeholder`. Intentional: no silent fallback to a wrong database. |
| Secrets (`*_CLIENT_SECRET`, `MW_*`, `AGENTAPP_PORTAL_TOKEN`) | **Startup fails.** Same reasoning. |
| External endpoints (`APS_BASE_URL`, `CARD_PROCESSOR_URL`, `GOOGLEAUTH_BASE_URL`) | **Startup fails.** These name third-party systems that have no sensible default. |
| Mail (`MAIL_*`) | Defaults to empty — mail is disabled, the service still starts. |
| Ports and service URLs | Sensible defaults; override only when they conflict or when running locally. |

---

## Service URLs

Which address a service uses to reach another depends on where it runs.

| Caller runs in | Address to use | Example |
| --- | --- | --- |
| Docker (Compose) | the compose service name + container port | `http://thirdparties:8008/thirdparties` |
| Host / IDE | `localhost` + **host** port | `http://localhost:18008/thirdparties` |

The compiled defaults are the Docker form, so Compose works with no overrides at all. Local runs
need the overrides from `.env.example`.

Three variables point at systems **outside** this repository and must be set to real endpoints —
never to a compose service name:

- `APS_BASE_URL` — account processing system
- `CARD_PROCESSOR_URL` — external card processor
- `GOOGLEAUTH_BASE_URL` — referenced by `app` and `backoffice`; that service is not part of this
  repository, and the auth paths using it fail until it is supplied.

---

## Ports

Host ports occupy the **18001-18016** block so they do not collide with anything already listening
on the standard 8000s. Container ports keep each service's original value.

Every host port is overridable in `.env`. If one is already taken:

```bash
echo "TRANSACTIONS_HOST_PORT=18209" >> .env
docker compose up -d transactions
```

Nothing else needs to change — services address each other by container port on the internal
network, so the host mapping is purely for access from your machine.

`switch-gateway` originally listened on **9994**, the same port as `nadra`. It now defaults to
**9995**; both are reachable, and `SWITCH_GATEWAY_BASE_URL` reflects the new value.

The simulator's ISO 8583 socket (`6661`) is separate from its HTTP port and is not published to
the host — `switch-gateway` reaches it over the internal network.

---

## Database

Oracle runs **outside** this project. There is deliberately no database container: these services
connect to an existing, separately administered Oracle instance holding real data.

```properties
DB_URL=jdbc:oracle:thin:@<host>:1521/<service-name>
DB_USERNAME=<schema>
DB_PASSWORD=<password>
```

The schema is managed externally. This repository contains **no migrations and no DDL**, and
nothing here creates or alters a table. A substantial amount of the business logic lives in Oracle
stored procedures (`PKG_MW`, `PKG_PAYMENTS1`); the services call them, they do not define them.

From a container, `localhost` is the container itself. If Oracle runs on your own machine, use
`host.docker.internal` (Docker Desktop) or the bridge gateway address (Linux) in `DB_URL`.

---

## Secrets

Rules for this repository:

- Real credentials live only in `.env`, which is git-ignored.
- `.env.example` carries placeholders, never values.
- The Firebase service-account key is **not** committed. `pushnotification` expects
  `src/main/resources/barakatpay-2e4d4-firebase-adminsdk-fbsvc-0c5b7fb862.json`; copy the
  `.json.example` template beside it to that exact name and paste in the real key before building.
  The filename is load-bearing — `FirebaseConfig` loads it from the classpath by name.
- Never log passwords, tokens, PINs, secrets or full customer identity data. Mask them.

> **Known exception.** `JwtConstants.AES_SECRET_KEY` and `IV_PARAM` are compile-time constants
> in every service. They are not externalised because the platform's AES-CBC encryption is
> deterministic by design — a fixed key and IV are what let the services match encrypted columns
> such as `TBL_CUSTOMER.NID_NO` on equality. Changing the key would invalidate every ciphertext
> already in the database, so rotating it requires a coordinated re-encryption of stored data and
> cannot be done from configuration alone. Treat access to this repository accordingly.

---

## Building

All 13 services build with Java 17:

```bash
mvn -f Transactions/pom.xml clean package        # one service
```

Or all of them:

```bash
for s in AgentApp App backoffice CardManagement pricingandcommission ThirdParties \
         Transactions Workflow pushnotification apigatway-encryption nadra \
         switch-gateway switch-simulator; do
  mvn -q -f "$s/pom.xml" clean package -DskipTests || echo "FAILED: $s"
done
```

Each produces a WAR in its own `target/`.

Note that `mvn compile` does **not** compile test sources — use `clean package` or `mvn test` so
test compilation errors surface. `Transactions` carries the test suite; run it with
`mvn -f Transactions/pom.xml test`.

---

## Troubleshooting

| Symptom | Cause and fix |
| --- | --- |
| `Could not resolve placeholder 'DB_URL'` | `.env` missing or the variable is blank. Copy `.env.example` and fill it in. |
| `ORA-01017: invalid username/password` | `DB_USERNAME` / `DB_PASSWORD` wrong, or pointed at the wrong Oracle instance. |
| `IO Error: The Network Adapter could not establish the connection` | `DB_URL` host unreachable from the container. If Oracle is on your host, use `host.docker.internal`. |
| `bind: address already in use` | Host port taken. Override the matching `*_HOST_PORT` in `.env`. |
| A service can't reach another (`UnknownHostException: thirdparties`) | Running on the host, not in Docker. Export the localhost overrides from `.env.example`. |
| Portal shows no KYC documents | `agentapp`/`app` write them and `backoffice` serves them, so all three must share the `./documents` bind mount. Without it each container keeps its own copy in an ephemeral layer. |
| `Report Folder Does Not Exist` | The Jasper report mount is missing; check the `JASPER REPORTS` bind mount in `docker-compose.yml`. |
| Firebase fails to start `pushnotification` | The service-account JSON is absent. See [Secrets](#secrets). |
| Build succeeds but the change isn't live | Incremental builds mask errors and stale classes. Always `clean`. |

---

## Repository layout

```
.
├── AgentApp/                 agent app backend
├── App/                      customer app backend
├── backoffice/               admin portal backend
├── CardManagement/           card management  (cardwrapper)
├── pricingandcommission/     fees and commission  (fee)
├── ThirdParties/             OTP, notifications, geocoding
├── Transactions/             transfers, IBFT, bill payment
├── Workflow/                 maker-checker workflow
├── pushnotification/         Firebase messaging
├── apigatway-encryption/     AES crypto gateway
├── nadra/                    CNIC verification
├── switch-gateway/           ISO 8583 / 1LINK adapter
├── switch-simulator/         1LINK simulator (development only)
├── JASPER REPORTS/           report definitions, mounted into backoffice
├── docs/                     migration reports, deployment notes, mobile integration guide
├── docker-compose.yml
├── .env.example
└── README.md
```

Each service directory holds its own `pom.xml` and `Dockerfile`. There is no parent POM — the
services are built and versioned independently.
