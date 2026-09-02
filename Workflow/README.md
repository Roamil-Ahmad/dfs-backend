# Workflow Engine

> Workflow / approval engine used by other services.

Part of the **DFS** platform — a suite of **12 Spring Boot microservices** that run together via Docker Compose.

---

## 📌 This service at a glance

| Property | Value |
|----------|-------|
| **Service** | Workflow Engine (`WorkFlow`) |
| **Port** | `8010` |
| **Context-path** | `/workflow` |
| **Swagger UI** | http://localhost:8010/workflow/swagger-ui/index.html |
| **Database** | Oracle — `jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV` |
| **Packaging** | WAR (runs standalone via `java -jar` in Docker; deploys to external Tomcat in prod) |

### 🔗 Dependencies
- **Calls →** —
- **Called by ←** AgentApp, BackOffice

---

## 🧰 Tech stack

- **Java 17** (Eclipse Temurin)
- **Maven 3.9.9** (via the bundled `./mvnw` wrapper)
- **Spring Boot 2.7.x**
- **springdoc-openapi** (Swagger UI) + Spring Cloud Sleuth (distributed tracing / correlated logs)

---

## 🌐 Platform — all services & ports

All 12 services use a clean sequential port scheme (`8001`–`8012`). Each service's port and
context-path live in its own `src/main/resources/application.properties` (single source of truth).

| # | Service | Folder | Port | Context-path | Database |
|---|---------|--------|------|--------------|----------|
| 1 | Agent App | `AgentApp` | 8001 | `/agentapp` | Oracle |
| 2 | Customer App API | `App` | 8002 | `/app` | Oracle |
| 3 | Back Office | `BackOffice` | 8003 | `/backoffice` | Oracle |
| 4 | Biometric SDKs | `bbs-sdks` | 8004 | `/bbs-sdks` | — |
| 5 | Card Management | `card-management` | 8005 | `/cardwrapper` | — |
| 6 | Google Auth (2FA) | `googleauth` | 8006 | `/googleauth` | Oracle |
| 7 | Pricing & Commission | `PricingAndCommision` | 8007 | `/fee` | Oracle |
| 8 | Third Parties | `ThirdParties` | 8008 | `/thirdparties` | Oracle |
| 9 | Transactions | `Transactions` | 8009 | `/transactions` | Oracle |
| 10 | Workflow Engine | `WorkFlow` | 8010 | `/workflow` | Oracle |
| 11 | API Gateway (AES) | `apigatway-encryption` | 8011 | `/barkat-crypto-gateway` | — |
| 12 | Push Notification | `pushnotification` | 8012 | `/firbase` | — |
| 13 | Cron Jobs | `cronjob` | 8013 | `/cronjobs` | Oracle |
| 14 | APS Client | `aps-client` | 8014 | `/apsclient` | — |

---

## 🏗️ Build (this service only)

```bash
./mvnw clean package -DskipTests
# artifact: target/*.war
```

## 🐳 Run the whole stack with Docker (recommended)

From the **repository root** (the `DFS` folder that contains all 12 projects + `docker-compose.yml`):

```bash
docker compose up -d --build      # build images + start all 12 containers
docker compose ps                 # check status
docker compose logs -f WorkFlow    # follow this service's logs
docker compose down               # stop & remove containers
```

Then open this service's Swagger UI:

```
http://localhost:8010/workflow/swagger-ui/index.html
```

---

## ⚙️ Configuration & inter-service wiring

- **Ports & context-paths** are defined in `application.properties` — the single source of truth
  (kept in sync with `docker-compose.yml` port mappings).
- **Inter-service URLs** use a placeholder with a production default, e.g.
  `${THIRDPARTIES_BASE_URL:http://192.168.0.4:8080}/thirdparties/...`:
  - **Outside Docker / prod** → falls back to the default host (no breakage).
  - **Inside Docker** → `docker-compose.yml` injects the container hostname
    (e.g. `http://thirdparties:8008`) so services reach each other over the `barakat-net` network.
- **All config & secrets are externalized to environment variables.** `application.properties`
  references `${VAR}` (secrets, no default) or `${VAR:default}` (config with safe fallback) — nothing
  is hardcoded. Real values live in a **gitignored `.env`** at the stack root (see `.env.example`),
  and `docker-compose.yml` loads it into every container via `env_file: .env`. **No secret is committed.**
  DevOps: copy `.env.example` to `.env`, fill the `# SECRET` values, then `docker compose up -d`.

---

*DFS · Java 17 · Maven 3.9.9 · Dockerized microservices*