# Barkat Crypto Gateway

An **AES-256-GCM encrypted API gateway** for BarkatPay. It sits in front of the
BarkatPay backend services and acts as a transparent, encrypting reverse proxy:
mobile clients talk to the gateway using encrypted envelopes, the gateway
decrypts, forwards the plaintext to the correct downstream service, then
re-encrypts the downstream response before returning it to the client.

- **Group / Artifact:** `com.barkatpay` / `barkat-crypto-gateway`
- **Version:** `1.0.0`
- **Packaging:** `war` (deployable to an external Tomcat, or runnable standalone)
- **Java:** 13
- **Framework:** Spring Boot 2.7.5

---

## How it works

```
                 ┌─────────────────────── Barkat Crypto Gateway ───────────────────────┐
   Mobile app    │                                                                     │   Downstream
  (encrypted)    │  Replay guard  →  AES-GCM decrypt  →  Route match  →  HTTP forward   │   services
  ────────────►  │      (timestamp)      (data + iv)       (prefix)        (plaintext)  │  ──────────►
                 │                                                                     │
  ◄────────────  │  AES-GCM encrypt  ◄──────────────────────────────────  response     │  ◄──────────
   (encrypted)   │      (fresh iv)                                         (plaintext)  │
                 └─────────────────────────────────────────────────────────────────────┘
```

1. **Replay protection** — the request `timestamp` must be within a configurable
   window (default **30s**) of server time, otherwise the request is rejected
   before decryption. See [`ReplayAttackGuard`](src/main/java/com/barkatpay/gateway/filter/ReplayAttackGuard.java).
2. **Decrypt** — the `data` + `iv` fields are AES-256-GCM decrypted to recover the
   plaintext JSON body. GCM's authentication tag guarantees integrity (tampered
   payloads are rejected). See [`AesGcmCryptoService`](src/main/java/com/barkatpay/gateway/crypto/AesGcmCryptoService.java).
3. **Route** — the request path (under `/gateway/**`) is matched against the
   configured route prefixes and forwarded to the matching downstream base URL.
   See [`RouteRegistry`](src/main/java/com/barkatpay/gateway/routing/RouteRegistry.java).
4. **Encrypt response** — the downstream plaintext response is AES-256-GCM
   encrypted with a **fresh random IV** and returned as an envelope.

### Request / response envelope

Both requests and responses use the same envelope shape:

```json
{
  "data": "<Base64 AES-256-GCM ciphertext (includes 128-bit auth tag)>",
  "iv": "<Base64 12-byte initialization vector>",
  "timestamp": "2026-07-05T10:15:30Z"
}
```

For `GET` requests the envelope fields may alternatively be supplied as query
parameters (`?data=...&iv=...&timestamp=...`).

---

## Project layout

```
src/main/java/com/barkatpay/gateway/
├── CryptoGatewayApplication.java     # Spring Boot entry point
├── ServletInitializer.java           # WAR deployment support
├── config/                           # Properties, HTTP client, Swagger, API inventory
├── controller/GatewayController.java # Catch-all /gateway/** for all HTTP verbs
├── crypto/AesGcmCryptoService.java   # AES-256-GCM encrypt / decrypt
├── dto/                              # EncryptedRequest / EncryptedResponse / error DTOs
├── exception/                        # Domain exceptions + global handler
├── filter/ReplayAttackGuard.java     # Timestamp replay-window validation
├── routing/                          # RouteDefinition + RouteRegistry
└── service/GatewayService.java       # Orchestrates decrypt → route → forward → encrypt
```

---

## Configuration

Configuration lives in [`src/main/resources/application.properties`](src/main/resources/application.properties).
All sensitive / environment-specific values are overridable via environment
variables.

| Property | Env var | Default | Description |
|----------|---------|---------|-------------|
| `server.port` | — | `8080` | HTTP port |
| `server.servlet.context-path` | — | `/barkat-crypto-gateway` | Base context path |
| `barkatpay.crypto.aes-key` | `BARKATPAY_AES_KEY` | *(baked default)* | Base64 AES-256 key (32 bytes) |
| `barkatpay.crypto.replay-window-seconds` | — | `30` | Allowed timestamp skew |
| `barkatpay.downstream.base-url` | `BARKATPAY_APP_BASE_URL` | `http://192.168.0.4:8080` | Default app backend |
| `barkatpay.downstream.dc-url` | `BARKATPAY_DC_BASE_URL` | `http://192.168.0.4:8080` | Transactions / cards / BBS backend |

### Routes

| Path prefix | Downstream | Strip prefix |
|-------------|-----------|--------------|
| `/gateway/transactions` | DC URL | `/gateway` |
| `/gateway/bbs-sdks` | DC URL | `/gateway` |
| `/gateway` (catch-all) | App base URL | `/gateway` |

> ⚠️ **Security note:** the AES key ships with a hardcoded default fallback for
> local development only. **In any shared or production environment, always set
> `BARKATPAY_AES_KEY` via the environment** and never rely on the baked default.

---

## Build & run

### Prerequisites

- JDK 13+
- Maven 3.6+

### Build

```bash
mvn clean package
```

This produces `target/barkat-crypto-gateway.war`.

### Run (standalone)

```bash
mvn spring-boot:run
```

The gateway is then available at:

```
http://localhost:8080/barkat-crypto-gateway/gateway/**
```

### Run (external Tomcat)

Deploy `target/barkat-crypto-gateway.war` to a servlet container. `ServletInitializer`
wires up the Spring Boot application for WAR deployment.

### Run with a custom key

```bash
export BARKATPAY_AES_KEY="<your-base64-32-byte-key>"
export BARKATPAY_DC_BASE_URL="http://your-dc-host:9600"
export BARKATPAY_APP_BASE_URL="http://your-app-host:8000"
mvn spring-boot:run
```

### Quick smoke test

Once running, verify the gateway is up:

```bash
# Health — expect {"status":"UP",...}
curl http://localhost:8080/barkat-crypto-gateway/actuator/health

# OpenAPI spec — expect HTTP 200 JSON
curl http://localhost:8080/barkat-crypto-gateway/v3/api-docs

# Gateway call — always returns HTTP 200 with an encrypted envelope
# (success or error; decrypt the body to inspect). A stale timestamp is
# rejected by the replay guard and returned as an encrypted error envelope.
curl -X POST http://localhost:8080/barkat-crypto-gateway/gateway/app/v1/getbalance \
     -H "Content-Type: application/json" \
     -d '{"data":"<base64-ciphertext>","iv":"<base64-iv>","timestamp":"2026-07-05T10:15:30Z"}'
```

---

## API documentation

Swagger UI and OpenAPI docs are exposed via springdoc (`springdoc-openapi-ui` 1.8.0):

- Swagger UI: `http://localhost:8080/barkat-crypto-gateway/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/barkat-crypto-gateway/v3/api-docs`

The Swagger UI includes an **Authorize** button (JWT bearer scheme) — the token
supplied there is forwarded to downstream services in the `Authorization` header.
Every route is documented from `api-inventory.json`, with the `EncryptedRequest` /
`EncryptedResponse` envelope schemas attached.

> 🔒 **Production:** set `springdoc.swagger-ui.enabled=false` — there is no Spring
> Security in front of the UI, so it is otherwise publicly reachable.

A Postman collection is also included: [`barkat-gateway.postman_collection.json`](barkat-gateway.postman_collection.json).

### Downstream API inventory

[`api-inventory.json`](api-inventory.json) documents the full set of BarkatPay
backend endpoints the gateway proxies (registration, KYC, balance, transfers,
cards, bill payments, BBS-SDK liveness, etc.), including methods, paths, request
and response shapes, and their source references in the mobile app.

---

## Distributed tracing & logging

Spring Cloud Sleuth is enabled so every log line carries the service name and
trace/span IDs, and the `traceId` propagates to downstream services for
end-to-end correlation across the shared logs.

- **Log format:** `yyyy-MM-dd HH:mm:ss.SSS [barkat-crypto-gateway] [traceId,spanId] LEVEL logger - message`
  (see [`logback-spring.xml`](src/main/resources/logback-spring.xml)).
- **Service name:** `spring.application.name=barkat-crypto-gateway` — the unique
  tag used to tell this service apart in the shared `catalina.out`.
- **Downstream propagation:** the gateway forwards over **Apache HttpClient 5**,
  which Sleuth does not auto-instrument. Trace headers (B3) are therefore
  injected manually onto each outgoing request in
  [`GatewayService.injectTraceContext`](src/main/java/com/barkatpay/gateway/service/GatewayService.java),
  so the same `traceId` appears in both the gateway's and the downstream
  service's logs.

## Health & monitoring

Spring Boot Actuator endpoints are enabled:

- Health: `http://localhost:8080/barkat-crypto-gateway/actuator/health`
- Info: `http://localhost:8080/barkat-crypto-gateway/actuator/info`

---

## Testing

```bash
mvn test
```

Unit tests (14) cover the crypto service, replay-attack guard, route registry,
and the global exception handler.

The build and app have also been verified end-to-end: `mvn clean test` passes,
the app boots, the actuator health and OpenAPI endpoints respond, a gateway call
returns an encrypted envelope, and every log line is stamped with
`[barkat-crypto-gateway] [traceId,spanId]`.

---

## Tech stack

- Spring Boot 2.7.5 (Web, Actuator)
- Spring Cloud Sleuth (distributed tracing)
- Apache HttpClient 5 (downstream forwarding)
- Jackson (JSON)
- springdoc-openapi 1.8.0 (Swagger UI)
- Lombok
- JUnit / Spring Boot Test
