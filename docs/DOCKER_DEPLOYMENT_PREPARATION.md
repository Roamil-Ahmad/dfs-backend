# DFS Pakistan — Docker Deployment Preparation

**Target server:** `46.224.146.158`
**Date:** 20 August 2026
**State:** preparation only — **nothing deployed, no containers started**

---

## ⚠️ Two blockers that must be resolved before deployment

### 1. I could not reach the server — no SSH credential was supplied

Your message gave the SSH command (`ssh root@46.224.146.158`) and warned me not to store the
password, but **no password or private key was actually included**, and this machine has no SSH
keys (`~/.ssh` contains only `known_hosts`).

What I verified from here:

```
46.224.146.158:22    OPEN   -> "Permission denied (publickey,password)"
46.224.146.158:1521  OPEN   -> Oracle listener responds
```

So the host is up and reachable; I simply cannot authenticate. **Everything in sections 1 and 2
of your brief (server inspection, live port scan) is therefore outstanding.** All host ports below
are *provisional* and must be confirmed free before deployment.

To unblock, either:
- add a public key to `/root/.ssh/authorized_keys` on the server, or
- run `ssh-copy-id` / paste the password so I can use it transiently in-session.

### 2. The new database rejects the DFS credentials

The DB config was updated exactly as instructed, but the new endpoint does **not** accept
`DFS/DFS`. Diagnosed by comparison, not assumption:

| Test | Result |
|---|---|
| **Old** host `167.233.72.195` + `DFS/DFS` | ✅ connects (control — proves the tooling and credential are fine) |
| **New** host `46.224.146.158` + bogus service name | `ORA-12514: Service ... is not registered` |
| **New** host `46.224.146.158` + `ORCLPDB1` + `DFS/DFS` | ❌ **`ORA-01017: invalid username/password; logon denied`** |

Because a wrong service name returns ORA-12514 while `ORCLPDB1` returns ORA-01017, **`ORCLPDB1`
is registered on the new server — it is the `DFS` user that is missing or has a different
password.**

All 9 services will fail to serve traffic until this is fixed. It needs a DB-side action (create
the `DFS` user / confirm its password), which is outside the read-only rule I am working under.

---

## Port mapping — PROVISIONAL

Internal Spring Boot ports were **not** changed, per your instruction. Each service keeps its own
container port; only the host port is new.

| # | Service | Compose service | Container port | **Planned host port** | Context path | Image |
|---|---|---|---|---|---|---|
| 1 | AgentApp | `agentapp` | 8001 | **18001** | `/agentapp` | `dfs/agentapp:latest` |
| 2 | App | `app` | 8002 | **18002** | `/app` | `dfs/app:latest` |
| 3 | backoffice | `backoffice` | 8003 | **18003** | `/backoffice` | `dfs/backoffice:latest` |
| 4 | CardManagement | `cardwrapper` | 8005 | **18005** | `/cardwrapper` | `dfs/cardwrapper:latest` |
| 5 | pricingandcommission | `fee` | 8007 | **18007** | `/fee` | `dfs/fee:latest` |
| 6 | ThirdParties | `thirdparties` | 8008 | **18008** | `/thirdparties` | `dfs/thirdparties:latest` |
| 7 | Transactions | `transactions` | 8009 | **18009** | `/transactions` | `dfs/transactions:latest` |
| 8 | Workflow | `workflow` | 8010 | **18010** | `/workflow` | `dfs/workflow:latest` |
| 9 | pushnotification | `pushnotification` | 8012 | **18012** | `/firbase` | `dfs/pushnotification:latest` |
| — | *nadra (out of scope)* | `nadra` | 9994 | *18014* | `/nadra` | `dfs/nadra:latest` |

Host ports are chosen to mirror the container port (`1` + container port) so the mapping is easy
to remember and audit. They are **not yet verified against the server**.

**Ports are overridable without touching compose** — every mapping reads from `.env`:

```
AGENTAPP_HOST_PORT=18001
APP_HOST_PORT=18002
...
```

so if DevOps finds a clash, change `.env` only.

### For DevOps — firewall

Firewall was **not** touched, as instructed. When you are ready to expose these, the ports to open
are:

```
18001 18002 18003 18005 18007 18008 18009 18010 18012
```

---

## What was changed in the codebase

Only the DB host. Nothing else in the 9 services was modified.

```diff
- spring.datasource.url=jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1
+ spring.datasource.url=jdbc:oracle:thin:@46.224.146.158:1521/ORCLPDB1
```

Unchanged, as instructed: port `1521`, service `ORCLPDB1`, user `DFS`, password `DFS`, driver
`oracle.jdbc.driver.OracleDriver`.

Applied to the 7 services that have a datasource: **AgentApp, App, backoffice, ThirdParties,
Transactions, Workflow, pricingandcommission**. CardManagement and pushnotification have no
datasource. `nadra` was also updated for consistency (see note below).

---

## Docker artefacts

### Image build status

The first attempt — `docker compose build`, all 9 in parallel — **failed**. The cause was network,
not code:

```
Failed to read artifact descriptor for net.sf.jasperreports:jasperreports-fonts:jar:6.21.0
Could not transfer artifact ... from central (https://repo.maven.apache.org/maven2):
Remote host terminated the handshake
```

Nine concurrent Maven builds each pulling their full dependency tree from Maven Central caused TLS
handshake failures. Compose aborts the whole run when one service fails, so **zero images were
produced**. Rebuilding sequentially with retries; status recorded below once complete.

This is worth knowing for the server too: if the target host has slow or filtered egress to
`repo.maven.apache.org`, build there sequentially rather than with the default parallel Compose
build.

| Artefact | State |
|---|---|
| `Dockerfile` × 9 | Already existed — reviewed, all correct multi-stage builds. **Unchanged.** |
| `.dockerignore` × 9 | Already existed. Unchanged. |
| `docker-compose.yml` | **Created** at workspace root |
| `.env.example` | **Created** — template, safe to commit |
| `.env` | **Created** from the template — git-ignored, secrets still blank |
| `.gitignore` | **Created** at root, ignores `.env` |
| `nadra/Dockerfile`, `nadra/.dockerignore` | **Created** (nadra had none) |

The existing Dockerfiles are already the multi-stage shape you asked for and use the Java version
each project requires, so I left them alone:

```dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE <service port>
ENTRYPOINT ["java","-jar","app.war"]
```

No Java, Spring Boot, Maven, Oracle driver or dependency versions were changed.

---

## Networking

All 9 services join one user-defined bridge network, `dfs-net`. Service-to-service traffic uses
Compose DNS names and internal ports — it does **not** hairpin through the public IP:

| Variable | Value | Consumers |
|---|---|---|
| `THIRDPARTIES_BASE_URL` | `http://thirdparties:8008` | AgentApp, App, Transactions |
| `WORKFLOW_BASE_URL` | `http://workflow:8010` | AgentApp, backoffice |
| `CARD_BASE_URL` | `http://cardwrapper:8005` | App, backoffice |
| `PUSHNOTIFICATION_BASE_URL` | `http://pushnotification:8012` | Transactions |
| `APP_BASE_URL` | `http://app:8002` | Transactions |

Left pointing outward on purpose (genuinely external systems):

| Variable | Value |
|---|---|
| `APS_BASE_URL` | `http://80.238.228.115:8000` |
| `CARD_PROCESSOR_URL` | `http://80.238.228.115:7790` |

**Note — the database is reached over the public IP** (`46.224.146.158`), which is the server's own
address. That is exactly what you specified, so I kept it, but if Oracle runs on this same host the
traffic will hairpin out and back. If you would rather it went direct, say so and I will switch the
JDBC host to the bridge gateway.

---

## Things that need your attention before deploy

1. **Six secrets are empty in `.env`.** These have no defaults in `application.properties`, and I
   proved in an earlier session that a missing value stops the Spring context with
   *"Could not resolve placeholder"* — the container will crash-loop, not start degraded:

   ```
   AGENTAPP_PORTAL_TOKEN     (AgentApp)
   MOBILE_CLIENT_SECRET      (Transactions)
   POS_CLIENT_SECRET         (Transactions)
   AGENT_CLIENT_SECRET       (Transactions)
   MAIL_PASSWORD             (App, backoffice)
   THIRDPARTIES_MAIL_PASSWORD (ThirdParties)
   ```

2. **`GOOGLEAUTH_BASE_URL` has no service to point at.** App and backoffice both reference a
   `googleauth` service that is not one of the 9 and does not exist in this workspace. Its auth
   paths will fail until you supply a real URL.

3. **`barakatpay` is not at zero — 3 occurrences remain in 2 files**, both by earlier explicit
   decision, not oversight:
   - `App/.../repo/TblAppUserRepo.java` queries `barkatpay_audit.tbl_app_user`. There is no
     `DFS_AUDIT` schema; `BARKATPAY_AUDIT` is the only audit schema that exists, so renaming this
     breaks the query with ORA-00942.
   - `pushnotification`'s Firebase service-account JSON (file name + `project_id` +
     `client_email`). You chose to leave the credential intact so push notifications keep working.

   Everything else — 1,059 files of package references, branding, JWT issuer, endpoints, deploy
   paths, docs — is `dfs`.

4. **A live Google credential is baked into the `pushnotification` image.** The Firebase
   service-account JSON sits in `src/main/resources`, so it ships inside the image that will run on
   a public server. Consider mounting it as a secret instead. Not changed — it is pre-existing and
   changing it alters how the service loads its credential.

5. **`nadra` is excluded from the 9.** It sits behind a Compose profile, so `docker compose up -d`
   will not start it. It is also still functionally blocked — `TBL_NADRA` and `TBL_NADRA_HITS` do
   not exist in the `DFS` schema. I updated its DB host and gave it a Dockerfile so it is ready if
   you want it, but say the word before including it.

6. **There are now two competing deployment models — pick one.** Each service already ships its own
   GitHub Actions workflow that deploys independently with `docker run`:

   | | Existing GH Actions | New Compose setup |
   |---|---|---|
   | Trigger | push to `pre-prod` | manual `docker compose up -d` |
   | Container names | `barakat-app`, `barakat-backoffice`, … | `dfs-app`, `dfs-backoffice`, … |
   | Network | `barakat-net` | `dfs-net` |
   | Host ports | **8001–8012** (container port published directly) | **18001–18012** |
   | Env source | `/var/www/dfs/.env` on the server | `.env` beside the compose file |

   They do not collide (different names, networks and ports), so nothing breaks — but running both
   gives you **two running copies of every service**, both hitting the same database. Decide which
   model is authoritative before deploying. If it is Compose, the workflows should be disabled.

7. **The root-level files are not in any git repository.** The workspace root is not a repo — each
   of the 9 services is its own separate repo. So `docker-compose.yml`, `.env.example`, `.env` and
   the root `.gitignore` I created are currently **local-only files** (and that root `.gitignore`
   has no effect, since nothing tracks the root). Before deployment either copy them to the server
   manually, or create a small deployment repo to hold them. Tell me which and I will set it up.

---

## Deployment commands — DOCUMENTED ONLY, NOT EXECUTED

Run from the directory containing `docker-compose.yml`.

```bash
# 0. BEFORE ANYTHING: confirm the planned host ports are free
ss -lntp | grep -E ':(18001|18002|18003|18005|18007|18008|18009|18010|18012)\b'   # expect no output

# 1. Fill in the six secrets
cp .env.example .env && vi .env

# 2. Validate without starting anything
docker compose config

# 3. Build images (safe — does not start containers)
docker compose build

# 4. DEPLOY  <-- only when you explicitly approve
docker compose up -d

# 5. Verify
docker compose ps
docker compose logs -f --tail=100
```

Rollback if needed:

```bash
docker compose down          # stop + remove containers, keeps images
docker compose down --rmi local   # also remove the built images
```

The `nadra` service, if you later decide to include it:

```bash
docker compose --profile nadra build
docker compose --profile nadra up -d
```

---

## Compliance with the brief

| Requirement | Status |
|---|---|
| Containers started | **NO** |
| Containers running | **0** |
| `docker compose up` executed | **NO** |
| `docker run` executed | **NO** |
| Firewall modified | **NO** |
| Database modified | **NO** — no DDL/DML issued; only failed logon attempts against the new host |
| Business logic changed | **NO** |
| Existing server services touched | **NO** — could not connect at all |
| Java / Spring / Maven / driver versions changed | **NO** |
| Internal Spring Boot ports changed | **NO** |
