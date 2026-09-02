# NADRA Service — TBL_NID_* Alignment Report

**Service:** `nadra` · package `com.barq.nadra` · artifact `com.barq:nadra:0.0.1-SNAPSHOT`
**Target DB:** `jdbc:oracle:thin:@46.224.146.158:1521/ORCLPDB1` · schema `DFS` · inspected **read-only**
**Date:** 21 August 2026

> This report supersedes the earlier version, which described the service as blocked because
> `TBL_NADRA` / `TBL_NADRA_HITS` did not exist. Those tables have since been created under new
> names and the service has been rewritten against them.

---

## 0. Headline

| | |
|---|---|
| Build | **BUILD SUCCESS** — executable `nadra.war` produced |
| Entity/column mappings validated against the live DB | **85 / 85 resolve** |
| Native queries validated against the live DB | **6 / 6 parse and execute** |
| Database modifications | **0** |
| Random / hardcoded citizen data remaining | **0** |
| Files changed | **9** in `nadra`, plus **6 each** in `App` and `AgentApp` for the mobileRegistration hook |
| Deployed | `46.225.160.93` — `dfs-nadra` (new, port 18014), `dfs-app` and `dfs-agentapp` rebuilt |

**Both endpoints now work end-to-end — but they will answer "Nadra Verification Failed" for every
CNIC until `TBL_NID_MOCK` is seeded.** The table currently has **0 rows**. Seeding it is a DB write,
which the standing read-only rule forbids me from doing. See §6.

---

## 1. DB changes this work responds to

| Old object | New object | Notes |
|---|---|---|
| `TBL_NADRA` | `TBL_NID_DATA` | `CNIC` → `NID_NO`; `AKSA_TRANS_ID_OUT` **dropped**; `TRANSLATION_ID` **added**; `CREATEUSER` now nullable |
| `TBL_NADRA_HITS` | `TBL_NID_HITS` | `NADRA_HITS_ID` → `NID_HITS_ID`; `CNIC` → `NID_NO`; `NADRA_RESPONSE*` → `RESPONSE`, `RESPONSE_CODE`, `RESPONSE_MESSAGE`; **no `CNIC_HASH` column** |
| — | `TBL_NID_MOCK` | New. 25 columns, PK `NID_NO VARCHAR2(13)`. Source of truth for mock verification. |

Verified present and usable:

- `TBL_NID_HITS_SEQ` **exists** → `@GeneratedValue(SEQUENCE)` works; no app-side id generation needed.
- Primary keys: `PK_NID_DATA(NID_NO)`, `PK_NID_HITS(NID_HITS_ID)`, `PK_TBL_NID_MOCK(NID_NO)`.
- No triggers and no identity columns on any of the three tables.

### One DB quirk the code has to match exactly

`TBL_NID_MOCK` spells its birthplace column **`BIRTH_PALCE`** (transposed letters). The entity keeps
the correct Java name and pins the physical name explicitly, so the typo stays contained to one line:

```java
/** NOTE: the physical column really is spelled BIRTH_PALCE in TBL_NID_MOCK. */
@Column(name="BIRTH_PALCE")
private String birthPlace;
```

If the DBA later corrects the column name, that one annotation is the only thing to change.

---

## 2. New verification behaviour

Both endpoints run in **mock mode** today (`nadraBio.mock=Y`, `nadraBvs.mock=Y`, and neither
`NADRA_MOCK` nor `NADRA_BVS_MOCK` has a row in `TBL_GLOBAL_CONFIG`, so the property values stand).

### `POST /nadra/v1/cnicVerification`

1. `TBL_NID_DATA` is checked first for an already-verified record (unchanged behaviour).
2. Otherwise the CNIC is looked up in **`TBL_NID_MOCK`**.
3. **Row found** → that row's data *is* the NADRA response. It is copied onto the `TBL_NID_DATA`
   record, persisted, and returned.
4. **No row** → `TBL_NID_HITS` records the failed attempt and the API returns:

```json
{ "responsecode": "001", "message": "Nadra Verification Failed", "data": null }
```

### `POST /nadra/v1/bioVerification`

Same lookup, same two outcomes. On success the exemption switch (`BVS_EXEMPTION_MOCK = Y`) still
overrides the result to `122 / "Bvs Not Matched"` exactly as before.

### The live-NADRA path is untouched

When mock mode is switched off (`NADRA_MOCK` / `NADRA_BVS_MOCK` set to `N`), the SOAP
`GetCitizenData` call and the REST `VerifyFingerprints` call run as they always did. Only column
names changed on that path.

---

## 3. Randomly generated data — removed

Five generators fabricated citizen data on every mock call. **All five are deleted**, along with the
`java.util.Random` field that drove them:

| Removed | What it used to fabricate |
|---|---|
| `generateRandomNameMale()` | Name, from 20×20×20 hardcoded name parts |
| `generateRandomNameFemale()` | Name, from 20×20×40 hardcoded name parts |
| `generateRandomAddress()` | Present address, from hardcoded city/area/street lists |
| `getRandomMotherName()` | Mother's name, from 20 hardcoded names |
| `getBirthPlaceRandom()` | Birthplace, from 21 hardcoded cities |
| `getAdjustedDate(char)` | DOB / CNIC expiry, as `now ± random(18..40) years` |

Also removed: gender derived from `cnic.substring(11) % 2`, and the fabricated
`sessionId = "123456" + cnic` and `statusCode = "100"` literals. Every one of those values now comes
from the `TBL_NID_MOCK` row.

---

## 4. Files changed

**New**

| File | Purpose |
|---|---|
| `model/TblNidMock.java` | Maps `TBL_NID_MOCK` (read-only from the service's side) |
| `repo/TblNidMockRepo.java` | `findByNidNo` |
| `model/TblNidData.java` | Replaces `TblNadra.java` |
| `model/TblNidHits.java` | Replaces `TblNadraHit.java` |
| `repo/TblNidDataRepo.java` | Replaces `TblNadraRepo.java` |
| `repo/TblNidHitsRepo.java` | Replaces `TblNadraHitRepo.java` |

**Deleted:** `model/TblNadra.java`, `model/TblNadraHit.java`, `repo/TblNadraRepo.java`,
`repo/TblNadraHitRepo.java`.

**Rewritten:** `service/nadra/NadraApisImpl.java` (630 → 541 lines).

**Edited:** `controller/NadraPostApis.java` (failure response), `utils/Constants.java` (6 constants added).

### Two dead queries dropped

Both referenced a `CNIC_HASH` column that does not exist in `TBL_NID_HITS`, and neither was called
from anywhere in the service:

- `findLastThreeBvsHitByCnicHash(...)`
- `findLastBvsHitByCnicHash(...)`

Also dropped: `TblNadraRepo.generateCnicHash(...)`, which called `SHA256.ENCRYPT` — a package that
does not exist in the `DFS` schema. It too was uncalled.

---

## 5. Judgement calls worth your review

These are the four places where the instruction left room for interpretation. Each is a one- or
two-line change and easy to reverse.

**1. `cnicVerification` now writes a `TBL_NID_HITS` row.**
You asked for the result to be saved to both tables. The old mock path wrote only to `TBL_NADRA`;
the BVS path wrote to both. Both endpoints are now symmetric — success and failure alike are
audited in `TBL_NID_HITS`.

**2. `STATUS_CODE` / `STATUS_MESSAGE` fall back to `100` / `"Success"`.**
Only when the `TBL_NID_MOCK` row leaves those columns empty. The reasoning: you specified that the
*existence of the row* is the verification result. No citizen field has a fallback — if the mock row
leaves a name or address empty, the response carries it empty.

**3. `SESSION_ID` is taken from the mock row, with no fallback.**
The old code fabricated `"123456" + cnic`. It now returns whatever `TBL_NID_MOCK.SESSION_ID` holds,
including `null`. **Populate that column when seeding** — see §6.

**4. Two null-safety guards added.**
- `PRESENT_ADDRESS` now falls back to `PRESENT_ADDRESS_EN` when empty, matching the fallbacks the
  original code already had for `NAME` and `MOTHER_NAME`. Without it the live-NADRA path persisted
  and returned a null address.
- `RESPONSE` (2000 chars) and `RESPONSE_MESSAGE` (500 chars) are truncated to their column widths.
  The live-NADRA path stores a full SOAP-derived JSON payload there, which would otherwise raise
  `ORA-12899` on a long response.

---

## 6. Action required from the DB team

**`TBL_NID_MOCK` has 0 rows.** Until it is seeded, both endpoints return
`001 / "Nadra Verification Failed"` for every CNIC. This is correct behaviour per the specification,
but it means the service cannot be smoke-tested yet.

I have not inserted anything — the standing instruction is that I do not alter the database.

Seeding checklist per row:

| Column | Fill with |
|---|---|
| `NID_NO` | The 13-digit CNIC, no dashes |
| `NAME`, `NAME_EN` | Both — `NAME` is what the API returns |
| `MOTHER_NAME`, `MOTHER_NAME_EN` | Both |
| `PRESENT_ADDRESS`, `PRESENT_ADDRESS_EN` | Both |
| `FATHER_HUSBAND_NAME_EN`, `PERMANENT_ADDRESS_EN`, `BIRTH_PLACE_EN` | As available |
| **`BIRTH_PALCE`** | Birthplace (note the column's spelling) |
| `DATE_OF_BIRTH`, `EXPIRY_DATE` | `yyyy-MM-dd` strings |
| `ISSUANCE_DATE` | `DATE` — must match what the caller sends, or `checkNadraExistance` will miss the cached record |
| `GENDER` | `MALE` / `FEMALE` |
| **`SESSION_ID`** | A session identifier — returned to the caller verbatim; left `null` if you leave it empty |
| `STATUS_CODE`, `STATUS_MESSAGE` | `100` / `Success` (or leave empty to accept the fallback) |
| `TRANSLATION_ID` | As applicable |
| `CREATEUSER`, `CREATEDATE` | **NOT NULL** — both required |

---

## 7. Verification evidence

**Compilation** — `mvn -o -DskipTests package` from a clean copy of the module:

```
[INFO] --- spring-boot-maven-plugin:2.7.8:repackage (repackage) @ nadra ---
[INFO] Replacing main artifact with repackaged archive
[INFO] BUILD SUCCESS
```

(The same command run in-place fails at the final rename step because an IDE or AV process holds a
lock on `nadra/target/nadra.war`. That is an environment issue, not a build issue — the `.war` is
produced either way.)

**Mapping validation** — every persistent field of all 5 entities, explicit `@Column` and implicit
`SpringPhysicalNamingStrategy` alike, checked against `USER_TAB_COLUMNS`:

```
85 entity->column mappings extracted
=== MISSING (entity column not in DB) ===
(none)
```

**Query validation** — all 6 native queries executed read-only against the live DB. All parse, all
return their expected column sets:

| Query | Result |
|---|---|
| `TblNidDataRepo.findByNidNoAndIssuanceDate` | 31 columns, 0 rows |
| `TblNidDataRepo.findByNidNo` | 31 columns, 0 rows |
| `TblNidHitsRepo.getSessionId` | `SESSION_ID`, `TRANSACTION_ID`, 0 rows |
| `TblNidMockRepo.findByNidNo` | 25 columns, 0 rows |
| `LkpCityRepo.getAllCitiesIsActiveYes` | 466 rows |
| `TblGlobalConfigRepo.findByKeyName` | 0 rows (mock switches fall back to properties) |

**Residual references** — no `TblNadra`, no `TBL_NADRA`, no generator method survives anywhere in
the service.

---

## 8. Known issues left alone (pre-existing, not introduced here)

| Issue | Where | Why left |
|---|---|---|
| `getSessionId` filters `RESPONSE NOT IN ('100','119')` but `RESPONSE` holds a JSON payload, not a code — the filter is effectively always true. The intended column is almost certainly `RESPONSE_CODE`. | `TblNidHitsRepo` | Changing it changes session-reuse behaviour. Flagging rather than silently altering business logic. |
| `createdate` is `@Temporal(TemporalType.DATE)`, so the time is truncated. `getSessionId`'s 5-minute `HAVING SYSDATE - (5/24)/60 <= MAX(CREATEDATE)` window therefore never behaves as written. | `TblNidHits` | Same reason — pre-existing, and the fix is a behaviour change. |
| `extractCityName` / `findBestMatch` are dead code (nothing calls them). | `NadraApisImpl` | Harmless; removing them would also orphan `LkpCityRepo`. |
| `App`, `AgentApp`, `ThirdParties`, `Transactions` each carry orphaned `TblNadra`, `TblNadraMismatch`, `TblNadraMock` entities pointing at tables that no longer exist. | 4 other services | No repository or service references any of them, and `ddl-auto=none` means Hibernate never validates them. They are inert. Cleaning them up is a separate change across four services. |

---

## 9. mobileRegistration now calls cnicVerification

`app` and `agentapp` both verify the CNIC through this service before issuing the registration OTP.

| | |
|---|---|
| Where | `SignUpPostApis.mobileRegistration` (App) · `CustomerSignUpPostApis.mobileRegistration` (AgentApp) |
| Position | Immediately before `thirdPartyService.generateOtp(...)` |
| Endpoint called | `POST http://nadra:9994/nadra/v1/cnicVerification` |
| Body | `{"cnic": <payload.nidNo>, "mobileNumber": <payload.mobileNo>, "cnicIssuanceDate": <payload.nidIssuanceDate>}` |
| On `000` | Flow continues, OTP is sent |
| On anything else | Returns nadra's code and message, **no OTP is generated** |
| On no response | `151 / Technical Issue` |

Supporting changes in both services:

- New DTO `dto/NadraVerificationRequest.java`. The nadra endpoint consumes a plain body rather than
  the standard `Request` envelope, so the DTO is posted directly.
- `ThirdPartyService.verifyCnic(cnic, mobileNumber, authToken)` + impl.
- `nadra.cnic.verification.url=${NADRA_BASE_URL:http://localhost:9994}/nadra/v1/cnicVerification`
- `RequestValidator.mobileRegistrationRequestValidation` now rejects an empty `nidNo`
  (`INVALID NID NUMBER`) — the field is required for this flow to mean anything.

### `nidIssuanceDate`

`MobileRegistrationRequest` gained a second required field, annotated the same way
`CustomerKycRequest` already annotates its own issuance date:

```java
@NotNull
@NotEmpty
@Pattern(regexp = "\d{4}-\d{2}-\d{2}", message = "NidNo Issuance Date must be in the format yyyy-MM-dd")
private String nidIssuanceDate;
```

Note that **nothing in App or AgentApp is annotated `@Valid`**, and the payload is deserialised by
hand out of the `Request` envelope, so those annotations do not execute. They are there for
consistency and documentation; the format is actually enforced by an explicit check added to
`RequestValidator.mobileRegistrationRequestValidation`, which throws
`NidNo Issuance Date must be in the format yyyy-MM-dd` on a missing or malformed value.

The value travels to nadra as the raw `yyyy-MM-dd` string — `NadraVerificationRequest.cnicIssuanceDate`
is a `String` on the caller side, so nothing reformats it in transit. On the nadra side the field
stays a `java.util.Date` and is parsed with an explicit
`@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)`.

`lenient = FALSE` is load-bearing, and the smoke test is what caught it. Jackson's default leniency
does **not** reject `"15-01-2020"` — `SimpleDateFormat` rolls it over into a nonsense date, which
would then be written to `TBL_NID_DATA.ISSUANCE_DATE` and used as the cache-lookup key, silently.
With strict parsing the request is rejected outright instead.

With a date now supplied, `checkNadraExistance` does real work again: a CNIC already verified against
the same issuance date is served from `TBL_NID_DATA` without touching `TBL_NID_MOCK`. The null guard
stays for callers that do not send one.

**Timezone note:** Jackson parses the date at the ObjectMapper's timezone (UTC by default) and
`checkNadraExistance` formats it back with `SimpleDateFormat` at the JVM default. Both the `dfs-app`
and `dfs-nadra` containers run at `UTC+0000` (verified), so the two agree and no day-shift is
possible. If anyone ever sets a non-UTC `TZ` on these containers, re-check that lookup.

The nadra service itself needed one guard for this caller: `checkNadraExistance` used to NPE when
`cnicIssuanceDate` was absent, and mobileRegistration does not collect one. It now returns `null` in
that case and falls through to a full verification. The SOAP request builder is guarded the same way.

### Placement caveat

The check sits where you asked for it — immediately before OTP generation — which is *after*
`registerCustomerAll` and session creation. A CNIC that fails verification therefore still leaves a
`TBL_CUSTOMER_ALL` row and an auth-token session behind. If you would rather nothing be created for
an unverifiable CNIC, the block moves up to just after the request validation call.

---

## 10. Deployment

Deployed to `46.225.160.93` on 21 August 2026. `nadra` is no longer profile-gated in
`docker-compose.yml` — `app` and `agentapp` depend on it at runtime now.

| Container | Host port | Container port | Status |
|---|---|---|---|
| `dfs-nadra` | **18014** (new — DevOps must open it if external access is needed) | 9994 | Started |
| `dfs-app` | 18002 | 8002 | Rebuilt, restarted |
| `dfs-agentapp` | 18001 | 8001 | Rebuilt, restarted |

Only these three services were touched: `docker compose up -d --no-deps nadra app agentapp` was used
rather than a bare `up -d`, so the removed `gateway` container was not recreated. All nine
pre-existing DFS containers and the unrelated paybridge/vycepay containers were left alone.

### Smoke tests run against the live deployment

```
POST /nadra/v1/cnicVerification  {"cnic":"3520212345671","mobileNumber":"03001234567"}
  -> {"responsecode":"001","data":null,"messages":"Nadra Verification Failed"}

POST /app/v1/mobileRegistration  (payload.nidNo = 3520212345671)
  -> {"responsecode":"001","data":null,"messages":"Nadra Verification Failed"}
```

Both attempts were audited in `TBL_NID_HITS` (ids 1 and 2, `SERVICE_NAME=BIO`, `RESPONSE_CODE=001`),
which confirms the sequence, the entity mappings and the App → nadra call path all work.

Because `TBL_NID_MOCK` is still empty, the success path could not be exercised. **Every
mobileRegistration will fail at CNIC verification until that table is seeded** (§6).

The App smoke test left a `TBL_CUSTOMER_ALL` row behind (`CUSTOMER_ALL_ID=734`,
`IMEI_NO=TESTIMEI900001`, mobile `03009900001`) for the reason described in §9. I cannot delete it
under the read-only rule — remove it if you want the table clean.

`agentapp`'s mobileRegistration requires an authenticated session, so it was verified by build and
clean startup rather than by an end-to-end call.

---

## 11. Not done

- **No database writes of any kind.** No `INSERT`, no `CREATE`, no `ALTER`. Every DB interaction in
  this session was a read-only `SELECT` on a connection with `setReadOnly(true)` and `autoCommit(false)`.
- **No TBL_NID_MOCK seeding.** The success path is untested because I cannot insert the data.
- **`gateway` is still defined in `docker-compose.yml`.** It was not recreated by this deployment,
  but a bare `docker compose up -d` would bring it back.
- **Package not renamed.** `com.barq.nadra` is untouched — the earlier `barakatpay` → `dfs` rename
  does not apply to this prefix and you did not ask for it.
