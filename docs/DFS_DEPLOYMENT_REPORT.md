# DFS Deployment Report — 46.225.160.93

**Date:** 20 August 2026
**App server:** `46.225.160.93` (`paybridge-app`), user `paybridge`
**Database:** `46.224.146.158:1521/ORCLPDB1` — unchanged, see note below
**Outcome:** **SUCCESS — 9 of 9 services running, NADRA excluded**

---

## Deployed services

| # | Service | Container | Image | Host port | Container port | Context path | Status |
|---|---|---|---|---|---|---|---|
| 1 | AgentApp | `dfs-agentapp` | `dfs/agentapp:latest` | **18001** | 8001 | `/agentapp` | **RUNNING** |
| 2 | App | `dfs-app` | `dfs/app:latest` | **18002** | 8002 | `/app` | **RUNNING** |
| 3 | backoffice | `dfs-backoffice` | `dfs/backoffice:latest` | **18003** | 8003 | `/backoffice` | **RUNNING** |
| 4 | CardManagement | `dfs-cardwrapper` | `dfs/cardwrapper:latest` | **18005** | 8005 | `/cardwrapper` | **RUNNING** |
| 5 | pricingandcommission | `dfs-fee` | `dfs/fee:latest` | **18007** | 8007 | `/fee` | **RUNNING** |
| 6 | ThirdParties | `dfs-thirdparties` | `dfs/thirdparties:latest` | **18008** | 8008 | `/thirdparties` | **RUNNING** |
| 7 | Transactions | `dfs-transactions` | `dfs/transactions:latest` | **18009** | 8009 | `/transactions` | **RUNNING** |
| 8 | Workflow | `dfs-workflow` | `dfs/workflow:latest` | **18010** | 8010 | `/workflow` | **RUNNING** |
| 9 | pushnotification | `dfs-pushnotification` | `dfs/pushnotification:latest` | **18012** | 8012 | `/firbase` | **RUNNING** |

Startup times: 15.0s – 31.0s. Internal Spring Boot ports unchanged.

### Ports for DevOps (firewall)

```
18001  18002  18003  18005  18007  18008  18009  18010  18012
```

Firewall **not modified**. All nine ports were confirmed free before deployment.

---

## NADRA — excluded

```
NADRA Service:    NOT DEPLOYED
NADRA Container:  NOT RUNNING   (none exists)
NADRA Image:      NOT BUILT     (none exists)
NADRA Source:     NOT ON SERVER (excluded from the transfer archive)
```

Three independent layers of exclusion:
1. `nadra/` was excluded from the source archive shipped to the server.
2. `nadra` sits behind a Compose profile, so `docker compose up -d` cannot start it.
3. The deploy command named all 9 services explicitly.

NADRA source remains intact in the local workspace.

---

## Database

| | |
|---|---|
| Host | **46.224.146.158** |
| Port | 1521 |
| Service | ORCLPDB1 |
| Username | DFS |
| **Connectivity** | **PASS — 7/7 DB services connected, 0 `ORA-` errors** |

### Why the DB host did not change

The instruction to "change properties and url as per new server" conflicts with the DB for one
concrete reason, verified rather than assumed:

- **No Oracle on `46.225.160.93`** — port 1521 closed, no oracle container present.
- **The database is still live on `46.224.146.158`** — `DFS/DFS` authenticates, 150 tables.
- **The new app server can reach it** (verified from the server itself).

Pointing the JDBC URL at `46.225.160.93` would break all seven DB-backed services. **The app server
moved; the database server did not.** If a DB is later stood up on the new host, changing this is a
one-line edit per service.

Hikari confirmed connected for: agentapp, app, backoffice, fee, thirdparties, transactions, workflow.
cardwrapper and pushnotification have no datasource by design.

---

## Service-to-service communication

All 9 containers share the `dfs-net` bridge network and resolve each other by Compose DNS — traffic
does **not** hairpin through the public IP.

Verified container-to-container over HTTP:

```
agentapp    -> http://thirdparties:8008   = 302
transactions-> http://app:2               = 302   (app:8002)
backoffice  -> http://workflow:8010       = 302
```

Wiring in use:

| Variable | Value | Consumers |
|---|---|---|
| `THIRDPARTIES_BASE_URL` | `http://thirdparties:8008` | AgentApp, App, Transactions |
| `WORKFLOW_BASE_URL` | `http://workflow:8010` | AgentApp, backoffice |
| `CARD_BASE_URL` | `http://cardwrapper:8005` | App, backoffice |
| `PUSHNOTIFICATION_BASE_URL` | `http://pushnotification:8012` | Transactions |
| `APP_BASE_URL` | `http://app:8002` | Transactions |

External systems left pointing outward: `APS_BASE_URL` (80.238.228.115:8000),
`CARD_PROCESSOR_URL` (80.238.228.115:7790).

---

## Endpoint health

| Service | swagger-ui | api-docs |
|---|---|---|
| agentapp | 302 | **200** |
| app | 302 | **200** |
| backoffice | 302 | **200** |
| cardwrapper | 302 | 500 ⚠ |
| fee | 302 | **200** (at its configured `/v1/api-docs`) |
| thirdparties | 302 | **200** |
| transactions | 302 | **200** |
| workflow | 302 | **200** |
| pushnotification | 302 | **200** |

302 on `swagger-ui.html` is the normal springdoc redirect.

⚠ **cardwrapper `/v3/api-docs` returns 500** — pre-existing dependency conflict, not a deployment
issue: `NoSuchMethodError: io.swagger.v3.oas.annotations.info.Info.summary()`, a swagger-annotations
vs springdoc version mismatch. Affects only the OpenAPI docs endpoint; the card APIs are unaffected.
Not fixed, since that would mean changing dependency versions.

---

## Verification summary

```
Total Services:            9 (+ NADRA excluded)
Deployed:                  9
Running:                   9/9
Failed:                    0
NADRA:                     Excluded — no source, no image, no container
DB Connectivity:           PASS (7/7 DB services, 0 ORA- errors)
Service-to-Service:        PASS (verified container-to-container HTTP)
Health Checks:             PASS (9/9 responding)
barakatpay References:     3 (2 files — deliberate, see below)
Old DB Host (167.233.72.195): 0
Database Modified:         NO
Business Logic Changed:    NO
Firewall Modified:         NO
Unrelated Applications:    UNTOUCHED (8 pre-existing containers, all still running)
```

### barakatpay — 3 references remain, by prior decision

- `App/.../TblAppUserRepo.java` → `barkatpay_audit.tbl_app_user`. No `DFS_AUDIT` schema exists;
  renaming breaks the query with ORA-00942.
- `pushnotification`'s Firebase service-account JSON (filename, `project_id`, `client_email`) —
  you chose to keep the live credential intact.

### Server capacity

| | |
|---|---|
| RAM | 15.2 GiB total, **8.4 GiB free before deploy** |
| After deploying 9 services | ~4.6 GiB still available |
| Swap | 0 (not needed here) |
| Pre-existing containers | 8, all untouched |
| Total running now | 17 (8 pre-existing + 9 DFS) |

This is the key difference from the previous server, which had only 251 MB free and could support
just 3 services.

---

## Outstanding items

1. **Secrets are placeholders.** `~/dfs/.env` contains `PLACEHOLDER_NOT_A_REAL_*` for
   `AGENTAPP_PORTAL_TOKEN`, `MOBILE_CLIENT_SECRET`, `POS_CLIENT_SECRET`, `AGENT_CLIENT_SECRET`,
   `MAIL_PASSWORD`, `THIRDPARTIES_MAIL_PASSWORD`. Services start fine, but the AgentApp portal
   integration, Transactions client-secret flows, and outbound mail **will fail at runtime** until
   real values are set. Update `~/dfs/.env` then `docker compose up -d` to apply.

2. **`GOOGLEAUTH_BASE_URL`** points at a non-existent host — that service is not among the nine.

3. **SMS is a no-op.** Twilio was removed from ThirdParties; `sendSms()` currently returns `true`
   without sending. Needs a real SMS provider implementation.

4. **Server change made:** `paybridge` was added to the `docker` group (one sudo use) so Docker
   could be driven without a password. Reversible: `sudo gpasswd -d paybridge docker`.
   The account already had sudo, so this grants no new privilege.

---

## Operating the deployment

```bash
ssh paybridge@46.225.160.93
cd ~/dfs

docker compose ps                    # status
docker compose logs -f --tail=100    # all logs
docker compose logs -f dfs-app       # one service

docker compose restart <service>     # restart one
docker compose down                  # stop + remove all 9 (NADRA unaffected)
docker compose up -d --no-build agentapp app backoffice cardwrapper fee \
                                 thirdparties transactions workflow pushnotification
```

Deployment directory: `~/dfs` (`/home/paybridge/dfs`), mode 700; `.env` mode 600.
