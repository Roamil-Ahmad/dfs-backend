# DFS Pakistan — Switching Layer & IBFT Integration Report

Scope: the two Zindigi-era services added to the repository (`onelinksimulator`,
`Communication-Service`) were analysed, renamed and refactored, and a new generic
transaction package with an IBFT implementation was added to the Transaction Layer.

Nothing was deployed. Nothing in the database was created, altered or written.

---

## 1. Existing service analysis

### 1.1 `onelinksimulator` (now `switch-simulator`)

**Purpose.** A stand-in for the real 1LINK switch. It opens an ISO 8583 TCP listener
(Apache MINA + jPOS `GenericPackager`, field definitions in `ISO8583.xml`), decodes what the
bank sends, and answers with a well-formed ISO 8583 response. It also exposes an HTTP
controller so an operator can push an *incoming* transaction into the bank on demand.

**Existing flow.**
1. MINA accepts a socket, `MockRequestDecoder` unpacks the ISO message into a `PDUWrapper`.
2. The processor branches on MTI and DE-03 processing code
   (`0800` network, `620000` account title inquiry, `480000` inter-bank transfer).
3. A response DTO is built, `MockResponseEncoder` packs it and writes it back on the same session.
4. For incoming traffic, the HTTP controller builds a `0200`/`0220`, writes it to the connected
   bank session, and parks the caller on an RRN-keyed pool until the `0210`/`0230` arrives.

**Reusable (kept).** The MINA server, codec factory, encoder/decoder, `ISO8583MessageParser`,
the `PDUWrapper` field model, the ISO field/processing-code enums, the RRN-keyed response pool,
`ISO8583Utils` / `FormatUtils` / `DateTools`. This is a correct and non-trivial 1LINK codec and
was worth keeping intact.

**Zindigi-specific (removed).** `TransactionServiceImpl` called a MicroBank core to resolve
account titles and balances, and fell back to hardcoded JS Bank account numbers and customer
names when the core did not answer. All of it is gone — the simulator no longer knows any
account, any title or any customer.

**Refactored.** `TransactionServiceImpl` was rewritten to be data-driven: it echoes the
DE-120 record data it was given, fills DE-43 from configuration, and returns the configured
DE-39. A new `RecordDataLayout` utility encodes/decodes the DE-120 layout from 1LINK v7.0
§9.62.1.1 (324-byte advice, 73-byte title fetch) instead of ad-hoc `substring` arithmetic.

### 1.2 `Communication-Service` (now `switch-gateway`)

**Purpose.** The only process that holds the TCP link to the switch. It turns JSON from the
Transaction Layer into ISO 8583 and back, and it correlates asynchronous socket responses to
the HTTP caller that is waiting for them.

**Existing flow.** REST in → validate → build ISO → write to socket → park on the response
pool keyed by RRN → the handler wakes the waiting thread when the matching `0210`/`0230`
arrives → JSON out. Incoming `0200`/`0220` from the switch were forwarded to the bank.

**Reusable (kept).** The MINA client and `ReconnectionFilter`, the codecs, the ISO parser,
the `BasePDU` field model and `*Build` request/response builders, the ISO enums, the DE-120
handling, and the response-correlation pool.

**Zindigi-specific (removed).** The 811-line `TransactionRouteServiceImpl` with its
maker-checker and back-office branches; the whole `com.mfs.commonservice` dependency and the
11 files that used it; 15 `TBL_SWITCH_*` / `LKP_SWITCH_*` entities and 14 repositories —
confirmed by query that **zero** such tables exist in the DFS schema; ActiveMQ; Jasypt; JJWT;
JavaMail; PrimeFaces; RxJava; and the `ZINDIGI` branding constant.

**Refactored.** `SwitchRouteServiceImpl` is a thin replacement for the old router.
`TransactionClientHandler` now only parks `0210`/`0230` by RRN and `0810` by STAN, and forwards
`0200`/`0220` to the Transaction Layer. `TransactionResponsePoolHandler` collapsed to a single
`awaitResponse(correlationKey)`. Socket configuration moved from the (non-existent) database
tables into `application.properties`.

---

## 2. Renaming

| | Old | New |
|---|---|---|
| Service 1 | `onelinksimulator` | `switch-simulator` |
| Artifact | `com.zindigi:onelinksimulator` | `com.dfs:switch-simulator` |
| Package | `com.zindigi.onelinksimulator` | `com.dfs.switchsimulator` |
| Main class | `OnelinksimulatorApplication` | `SwitchSimulatorApplication` |
| Server bean | `OneLinkMockServer` | `SwitchMockServer` |
| Processor | `OneLinkMessageProcessor` | `SwitchMessageProcessor` |
| Service 2 | `Communication-Service` | `switch-gateway` |
| Artifact | `com.zindigi:communication-service` | `com.dfs:switch-gateway` |
| Package | `com.zindigi.communicationservice` | `com.dfs.switchgateway` |
| Main class | `CommunicationServiceApplication` | `SwitchGatewayApplication` |
| Controller | `TransactionRouteController` | `SwitchGatewayController` |
| Router | `TransactionRouteService(Impl)` | `SwitchRouteService(Impl)` |

No Zindigi or JS Bank naming survives in either service; an architecture test asserts this.

---

## 3. Transaction architecture

A generic switching package was added to the Transaction Layer. **No existing transaction API
was changed** — every file is new and lives under `com.wallet.transaction.switching`.

```
com.wallet.transaction.switching
├── common/                      shared by all switch transaction types
│   ├── SwitchTransactionType    IBFT | BILL_PAYMENT | WALLET_TO_WALLET
│   ├── SwitchingConstants       DE-39 response codes, DE-03 processing codes
│   ├── ProcedureResult          OUT parameters of a switch stored procedure
│   ├── SwitchReferenceGenerator STAN and RRN per 1LINK §9.28  (y + ddd + hh + STAN)
│   ├── model/repo/service       TBL_TRANSACTION_MOCK, read-only
│   └── client/                  SwitchGatewayClient + gateway request/response DTOs
├── ibft/                        both directions, so it splits into two sub-packages
│   ├── IbftProcedureRequest     the 32 IN / IN-OUT parameters
│   ├── IbftProcedureService     PKG_MW.OUTGOING_IBFT and PKG_MW.INCOMING_IBFT
│   ├── IbftRequestValidator
│   ├── outgoing/                request, responses, service, controller
│   └── incoming/                message, service, controller
├── billpayment/                 one direction only, so it stays flat — no outgoing/incoming
│   ├── BillPaymentRequest           utilityCompanyCode + consumerNo + amount
│   ├── BillInquiryResponse / BillPaymentResponse
│   ├── BillPaymentProcedureRequest / BillPaymentProcedureService
│   ├── BillPaymentRequestValidator
│   └── BillPaymentService / BillPaymentController
└── wallettowallet/              placeholder — structure only, not implemented
```

The three transaction types share `common/`. IBFT and Bill Payment have behaviour;
Wallet-to-Wallet is declared in the enum and holds a placeholder, so adding it later is a new
sub-package, not a redesign. Bill Payment reuses `common/` unchanged — the mock lookup, the
reference generator, the PAN formatter and the gateway client all took a second flow without
being modified, which is what the generic package was for.

**Outgoing IBFT** (DFS customer sends money to another bank):

```
POST /transactions/v1/ibft/titleFetch
POST /transactions/v1/ibft/advice
        │
        ├─ validate
        ├─ TBL_TRANSACTION_MOCK lookup  (transactionType='IBFT', beneficiary account)
        ├─ POST switch-gateway /outgoing/title-fetch | /outgoing/advice
        │        └─ ISO 8583 over TCP → switch-simulator → response by RRN
        └─ PKG_MW.OUTGOING_IBFT   ← the debit happens here, in the database
```

**Incoming IBFT** (another bank sends money to a DFS customer):

```
switch-simulator ── 0200/0220 ──▶ switch-gateway ──▶ POST /transactions/v1/ibft/incoming/{titleFetch,advice}
                                                              └─ PKG_MW.INCOMING_IBFT
                 ◀── 0210/0230 ── DE-39 from the procedure ◀──
```

**Utility Bill Payment** (DFS customer settles a bill at another institution):

```
POST /transactions/v1/billPayment/billInquiry     ← no procedure, no money moves
POST /transactions/v1/billPayment/billPayment
        │
        ├─ validate
        ├─ TBL_TRANSACTION_MOCK lookup  (transactionType='BILL_PAYMENT',
        │                                UTILITY_COMPANY_CODE + UTILITY_CONSUMER_ID)
        │        └─ BILL_PAID = 'Y' → DE-39 94, refused before anything is debited
        ├─ PKG_PAYMENTS1.BILL_PAYMENT_ACQUIRER   ← the debit happens here, in the database
        └─ POST switch-gateway /outgoing/bill-inquiry | /outgoing/bill-payment
                 └─ ISO 8583 over TCP → switch-simulator → response by RRN
```

There is **no incoming bill payment or inquiry**: DFS is only ever the acquirer for a bill, so
the switch never originates one of these towards us. Nothing was built for that direction.

No debit, credit, posting, reversal or compensation is written in Java on any path.
`IbftProcedureService` and `BillPaymentProcedureService` prepare parameters, execute the call and
read the OUT parameters. Neither writes `BILL_PAID` back — `TBL_TRANSACTION_MOCK` stays read-only.

**Response shape.** All four endpoints answer with the same envelope the existing app transaction
APIs use (`FTPostApiController` → `CommonService.getResponse`):

```json
{ "responsecode": "000", "messages": "SUCCESS", "data": { ... } }
```

`responsecode` is the DFS generic code — `000` SUCCESS, `134` Invalid Account Information Provided
(no `TBL_TRANSACTION_MOCK` row), `151` Technical Issue — and `messages` is its `TBL_MESSAGE`
description; all three codes already exist in that table. The ISO 8583 DE-39 is never the envelope
code: it stays on the payload (`data.responseCode` on title fetch, `data.switchResponseCode` on
transfer, `data.responseCode` on both incoming endpoints). On the incoming path the message the
gateway sends back down the link is `data`, which `TransactionLayerClient` unwraps through
`TransactionLayerResponse`.

---

## 4. Database integration

**Read-only. No DDL, no DML.** The only statements this code issues are one `SELECT` and two
`CALL`s. Verified after the work: `PKG_MW` unchanged (spec VALID / body INVALID),
`TBL_TRANSACTION_MOCK` unchanged (0 rows), no `TBL_SWITCH_*` table created.

### 4.1 `TBL_TRANSACTION_MOCK`

Outgoing IBFT looks up exactly one row:

```sql
SELECT * FROM TBL_TRANSACTION_MOCK M
 WHERE M.TRANSACTION_TYPE = :transactionType          -- 'IBFT'
   AND TRIM(M.BENEFICIARY_ACCOUNT_NO) = TRIM(:beneficiaryAccountNo)
```

The beneficiary account comes from the request. `TRIM` on both sides so a padded IBAN and the
same IBAN typed by a user resolve to the same row. If no row matches, the transaction is
declined with DE-39 `68` — **no beneficiary name, account, bank or IMD is hardcoded anywhere**,
and none is invented. The repository has no write method by design.

### 4.2 Stored procedures

| | Outgoing | Incoming |
|---|---|---|
| Procedure | `PKG_MW.OUTGOING_IBFT` | `PKG_MW.INCOMING_IBFT` |
| Parameters | 38 (32 IN, 2 IN OUT, 6 OUT) | 38 (32 IN, 2 IN OUT, 6 OUT) |
| Param 18 / 19 | name-location / terminal id | terminal id / name-location |
| `P_IDENTIFIER` | `VARCHAR2` | `CHAR` |
| IN OUT | 26 `P_RECORDDATA`, 28 `P_UDF1` | same |
| OUT | 33 auth id, 34 error, 35 description, 36 status, 37 checkpoint, 38 trans head id | same |

The 18/19 swap is real — the two procedures genuinely declare those parameters in opposite
order — and is handled explicitly rather than papered over.

**Known database defects (pre-existing, not introduced here, not worked around).**
`PKG_MW` has a VALID specification and an **INVALID body**, so both calls currently fail with
ORA-04063 / ORA-06508. The reported body errors are:

```
ORA-00904 "CREATEUSERTYPE": invalid identifier           (lines 243, 262, 286, 304)
ORA-00904 "D"."STATUS": invalid identifier               (line 365)
ORA-00904 "C"."CHARGES_APPLICABLE": invalid identifier   (line 385)
PLS-00341  cursor C_CHARGES_APPLICABLE malformed
PLS-00341  cursor C_TRANS_CHARGES malformed
PLS-00320  x 2
```

The failure is caught, logged with the real Oracle message and surfaced as DE-39 `46`
(unable to process). Nothing is faked and no success is fabricated. Once the database team
recompiles the body, these same calls consume the real response with no Java change.

**Also outstanding:** `TBL_TRANSACTION_MOCK` is empty, so until it is seeded every outgoing
IBFT declines with DE-39 `68` by design. Seeding it is a database-team action.

---

## 5. Removed components — explicit confirmation

| Component | Status |
|---|---|
| Retry mechanism | **REMOVED.** No `@Retryable`, no `RetryTemplate`, no retry loop. A transaction is never replayed. The only reconnection left is `ReconnectionFilter`, which re-establishes the TCP link — transport recovery, not transaction retry. |
| `TBLSwitchSaf` | **REMOVED.** Entity, repository and every reference deleted. |
| SAF (store-and-forward) | **NOT IMPLEMENTED.** No queue, no replay, no parked-message store. |
| Elastic API logging | **REMOVED.** No Elasticsearch client, no HTTP log shipping, no `elk`/`elastic` configuration. |
| Proxy | **REMOVED.** All services run on the same host; the gateway dials the switch and the transaction layer directly. No `java.net.Proxy`, no `setProxy`, no proxy properties. |
| Zindigi-specific communication logic | **REMOVED / REFACTORED.** MicroBank core calls, maker-checker and back-office routing, `com.mfs.commonservice`, the `ZINDIGI` constant and 15 `TBL_SWITCH_*` entities are gone; the remaining ISO 8583 transport was refactored, not rewritten. |
| Hardcoded accounts / titles / balances | **REMOVED** from the simulator and never introduced in the gateway or transaction layer. |

Eight architecture-guard tests read the source tree and fail the build if any of these return.

---

## 6. Logging

Plain slf4j (`LoggerFactory.getLogger`) throughout both services and the new package —
no custom appender, no HTTP shipping, no Elastic.

Logged: MTI, STAN, RRN, processing code, response code, procedure status/checkpoint,
`TRANS_HEAD_ID`, timings. This is what is needed to trace a transaction end to end.

Not logged: passwords, tokens, PINs, client secrets, full credentials. `clientSecret` is read
from configuration into the procedure parameter and never reaches a log statement.

---

## 7. Configuration

All mock and technical configuration is in `application.properties`. No transaction data —
no account, amount or customer value — appears in any property file.

**`switch-simulator`** — port 9993, ISO listener 6661, acquirer id, forwarding institution id,
DE-43 card acceptor name/city/country, currency, default DE-39.

**`switch-gateway`** — port 9994, switch host/port and timeouts, the two incoming transaction-layer
URLs, and the fixed 1LINK institution values (DE-32 acquirer id, DE-24 network identifier,
DE-18 merchant type, DE-49 currency, sender country, originator details). No datasource, no
proxy, no elastic, no kafka, no SAF, no retry entries.

**`Transactions`** — the two `switch.gateway.*` URLs plus a timeout, and the `onelink.*`
technical values (acquirer id, network identifier, merchant type, POS entry mode, currency,
terminal id, card acceptor identification code, DE-43 name-and-location).

Hosts and ports are overridable by environment variable (`SWITCH_SIMULATOR_HOST`,
`TRANSACTIONS_BASE_URL`, `SWITCH_GATEWAY_BASE_URL`) so nothing needs editing per environment.

---

## 8. Testing

All 48 tests pass; all three modules package cleanly.

| Module | Tests | Result |
|---|---|---|
| `switch-simulator` | `RecordDataLayoutTest` (8) | 8 / 0 failures — BUILD SUCCESS |
| `switch-gateway` | `RemovedComponentsTest` (8), `RecordDataLayoutTest` (8) | 16 / 0 failures — BUILD SUCCESS |
| `Transactions` | `OutgoingIbftServiceTest` (10), `IncomingIbftServiceTest` (6), `IbftRequestValidatorTest` (8) | 24 / 0 failures — BUILD SUCCESS |

What they cover: the DE-120 layout at the exact offsets 1LINK v7.0 §9.62.1.1 defines
(50–61 source IMD, 61–72 destination IMD, 72–73 identifier, 118–148, 280–295, total 324);
the `TBL_TRANSACTION_MOCK` lookup including the empty-table decline path; that no beneficiary
data is fabricated; the 18/19 parameter swap between the two procedures; DE-39 mapping including
the broken-procedure fallback to `46`; validation rules; and the architecture guards for
retry / SAF / Elastic / proxy / Zindigi naming.

**Not covered by tests, by necessity:** an end-to-end run against `PKG_MW`. The package body is
invalid and the mock table is empty, so a green end-to-end test today would only prove that the
failure path works — which is exactly what `adviceFallsBackToUnableToProcess` asserts.

---

## 9. What was not done, deliberately

- **Nothing deployed.** No `docker build`, no `docker run`, no `docker compose`, no connection
  to any deployment host.
- **Database untouched.** No `CREATE`, `ALTER`, `DROP`, `TRUNCATE`, `INSERT`, `UPDATE`, `DELETE`.
  `TBL_TRANSACTION_MOCK` and both procedures are exactly as they were.
- **No existing transaction API changed.** The only non-additive edits in `Transactions` are two
  appended property blocks and a `spring-boot-starter-test` dependency in the POM.
- **Wallet-to-Wallet not implemented** — structure only, as instructed.
- **Bill Payment implemented for the outgoing (acquirer) direction only.** There is no incoming
  bill inquiry or bill payment: the switch never originates one towards DFS.
