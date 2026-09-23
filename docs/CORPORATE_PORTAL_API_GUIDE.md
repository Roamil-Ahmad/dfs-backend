# Corporate Portal — API integration guide

**For:** the Corporate Portal team
**Services:** `agentapp` (port 18001), `app` (18002), `transactions` (18009)
**Status:** live on the server.

---

## Why these endpoints exist

The mobile endpoints authenticate an **app login token** — issued by the mobile login flow and tied
to a registered handset. The Corporate Portal is a server-side web application: no handset, no
device binding, no app login, so it can never hold that token.

Rather than weaken the mobile APIs, each needed endpoint has a **portal twin** under
`/v1/corporate/…`. The twin runs the *same validator and the same service method* as the mobile
original — the business rules are one implementation, so the two cannot drift apart. Only the
credential presented at the door differs.

**The mobile endpoints are unchanged.** They still require a bearer token, and the portal key gives
no access to them.

---

## Authentication

Every `/v1/corporate/…` endpoint expects one header:

```
X-Portal-Key: <the shared key>
```

The key lives in the server's `.env` as `CORPORATE_PORTAL_API_KEY`. Get it from whoever owns the
deployment; it is not written down in this repository.

| Case | Response |
| --- | --- |
| Header missing | `{"responsecode":"406","messages":"Invalid Token"}` |
| Header wrong | `{"responsecode":"406","messages":"Invalid Token"}` |

Two things to be clear about:

- **The key authorises the caller as the portal. It does not select an account.** Every request
  still names its own subject in the payload, exactly as the mobile calls do.
- **The key is a bearer secret for money movement.** Keep it server-side. It must never reach a
  browser, a mobile bundle, or a log. Send it only over the internal network or TLS.

### The MPIN is still required

Dropping the login token does **not** drop the MPIN. `fundsTransferLocal` still verifies the
**customer's** MPIN before moving money — it is checked by `app`, against the same stored credential
the mobile app checks. A wrong MPIN is refused exactly as on mobile:

```json
{ "responsecode": "113", "data": null, "messages": "Wrong Mpin" }
```

---

## Request envelope

Every POST uses the platform's standard envelope. Only `payload` changes between endpoints.

```json
{
  "channel": "MOB",
  "imieNo": "",
  "latitude": "",
  "longitude": "",
  "language": "",
  "payload": { }
}
```

`imieNo` is a device id the shared validators insist on. **You may leave it empty or omit it** — the
portal endpoints substitute a fixed marker, and no service reads the value. Everything else is
optional unless an endpoint says otherwise.

Responses are always the same three keys:

```json
{ "responsecode": "000", "data": { }, "messages": "SUCCESS" }
```

`responsecode` `000` is success. Anything else is a business outcome carrying its reason in
`messages` — **including HTTP 200 responses**. Never treat HTTP 200 alone as success; always read
`responsecode`.

---

## Endpoints

### Account — `agentapp`, base `http://<host>:18001/agentapp`

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/v1/corporate/getbalance` | Account balance |
| POST | `/v1/corporate/miniStatment` | Recent transactions |
| POST | `/v1/corporate/changempin` | Change an MPIN |

Note the spelling `miniStatment` — it matches the existing mobile endpoint and is deliberate.

`getbalance` and `miniStatment` match on **mobile number *and* account level together**. A level
that does not match the account returns `USER NOT FOUND`, not an empty result — easy to misread as
a broken endpoint.

```jsonc
// getbalance / miniStatment
{ "mobileNumber": "0300xxxxxxx", "accountLevelCode": "L1" }

// miniStatment with a date range — both dates together, max 180 days apart
{ "mobileNumber": "0300xxxxxxx", "accountLevelCode": "L1",
  "fromDate": "2026-08-01 00:00:00", "toDate": "2026-09-21 23:59:59" }
// omit both dates entirely to get the last nine transactions

// changempin
{ "mobileNumber": "0300xxxxxxx", "currentMpin": "1234",
  "newMpin": "4321", "confirmMpin": "4321" }
```

### Customer MPIN — `app`, base `http://<host>:18002/app`

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/v1/corporate/mpinVerification` | Verify a customer MPIN |

```jsonc
{ "mobileNumber": "0300xxxxxxx", "mpin": "1234" }
```

You rarely need to call this directly — `fundsTransferLocal` calls it for you. It is exposed so the
portal can confirm an MPIN before showing a confirmation screen.

`000` = correct, `127` = wrong MPIN, `125` = no such customer.

### Transactions — base `http://<host>:18009/transactions`

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/v1/corporate/ibft/bankList` | Banks available for interbank transfer |
| POST | `/v1/corporate/ibft/titleFetch` | Resolve a beneficiary at another bank |
| POST | `/v1/corporate/ibft/advice` | Execute the interbank transfer |
| GET | `/v1/corporate/getbiller` | Biller list |
| POST | `/v1/corporate/billInquiry` | What a consumer owes |
| POST | `/v1/corporate/billPayment` | Pay that bill |
| POST | `/v1/corporate/initiateLocalFT` | Resolve beneficiary + fee, same-platform |
| POST | `/v1/corporate/fundsTransferLocal` | Execute the same-platform transfer |

`bankList` takes an empty payload (`{}`) but is still a **POST**, matching its mobile twin.
`getbiller` is the only **GET**, and still needs the header.

---

## The two transfer journeys

Both are two-step: resolve first, then commit. Never skip the first call — it is what gives the
customer the beneficiary name and the fee to confirm.

### Interbank (IBFT) — money to another bank

**1 · `bankList`** → `{}`. Returns `[{ "bankImd": "505895", "bankName": "…" }, …]`.
Keep the `bankImd` of the chosen bank; both later calls need it.

**2 · `ibft/titleFetch`**

```jsonc
{
  "fromAccountNo": "0300xxxxxxx",          // required — the payer's wallet
  "fromAccountNid": "3520112345671",       // required — payer's NID
  "beneficiaryAccountNo": "PK36SCBL…",     // required — IBAN or account at the target bank
  "beneficiaryBankImd": "505895",          // required — from bankList
  "amount": "100"
}
```

Show the returned beneficiary title to the customer for confirmation.

**3 · `ibft/advice`** — the same fields, with `amount` now required, plus optional
`purposeOfPayment` and `transactionReference`. This moves the money.

### Same-platform (local FT) — money to another DFS wallet

**1 · `initiateLocalFT`**

```jsonc
{
  "mobileNumber": "0300xxxxxxx",   // the payer
  "nidNo": "3520112345671",
  "accountNo": "0300yyyyyyy",      // the beneficiary
  "amount": "100",
  "accountType": "W"
}
```

Returns the beneficiary title, the fee, the available balance and an `authIdResponse`.

> **Do not send `type: "QR"` from the portal.** That branch raises an SMS OTP and needs a mobile
> login token the portal does not have. Any other type, or omitting it, is fine.

**2 · `fundsTransferLocal`**

```jsonc
{
  "mobileNumber": "0300xxxxxxx",   // required — the payer
  "nidNo": "3520112345671",        // required
  "accountNo": "0300yyyyyyy",      // required — the beneficiary
  "accountType": "W",              // required
  "amount": "100",                 // required
  "appUserId": "326",              // required — the payer's app user id
  "mpin": "1234",                  // the customer's MPIN; verified before the money moves
  "transPurposeId": "1",
  "narration": "optional"
}
```

`appUserId` is required by the shared validator and is **not** derivable from the portal key. Carry
it in your own customer record, or read it once per customer and cache it.

---

## Bill payment

**1 · `getbiller`** (GET) → `[{ "name": "…", "code": "BSS00001", "id": 25 }, …]`.
The `code` is the `utilityCompanyCode` below — **not** `billerCode`.

**2 · `billInquiry`**

```jsonc
{
  "fromAccountNo": "0300xxxxxxx",     // required
  "utilityCompanyCode": "BSS00001",   // required — from getbiller
  "consumerNo": "1234567890"          // required
}
```

**3 · `billPayment`** — the same three fields plus `fromAccountNid` and `amount`, both required.
Amount must parse as a number and be greater than zero.

---

## Corporate onboarding

`POST /agentapp/v1/corporateonboarding` creates a corporate agent in one call — device
registration, verification and KYC together, with the OTP left out.

It is the one endpoint here that takes **no `X-Portal-Key` and no bearer token**. It authenticates
no header at all, which is the same position `agentDeviceRegistration` was always in.

### `segment` is mandatory, and it lives on the envelope

```jsonc
{
  "channel": "AGNT",
  "segment": "Corporate Clients",   // <- required, ENVELOPE level, not inside payload
  "imieNo": "12ad5b4525363f4f",
  "payload": { /* KYC fields */ }
}
```

Omit it, or send only spaces, and the call is refused before anything is written:

```json
{ "responsecode": "111", "data": null, "messages": "Segment Required" }
```

What happens to the value:

- It is matched against `LKP_SEGMENT.SEGMENT_DESCR`, **ignoring case and surrounding spaces** —
  `"segment1"`, `"SEGMENT1"` and `"  Segment1 "` all resolve to the same existing row.
- A match is reused and tagged onto the new agent.
- No match creates the segment, with a four-character code derived from the name
  (`"Corporate Clients"` → `CORP`), then tags it on.

Send the same name consistently. Every distinct spelling that is not merely a case or spacing
difference creates a **new** segment, so `"Corporate"` and `"Corporates"` become two.

> The tag is written to `TBL_AGENT.SEGMENT_ID`. `TBL_ACCOUNT` has no segment column — the agent is
> what the new account hangs off, so that is where the segment lives.

### Optional hierarchy fields

| Field | Effect |
| --- | --- |
| `parentAgentId` | Makes the new agent a sub-agent. `AGENT_TYPE` becomes `C`; a standalone agent gets `P`. |
| `parentCommission` | Percentage the parent earns, written to `TBL_AGENT_COMMISSION_DISTRIBUTION` at level 1. Needs `parentAgentId` to mean anything. |
| `partners` | A list of `{email, password}`. Each becomes an app user of its own against the same agent. |

A `parentAgentId` naming an agent that is **itself** a sub-agent is refused, and nothing is written:

```json
{ "responsecode": "163", "messages": "A child agent cannot be assigned as a parent agent." }
```

### Response

```json
{ "responsecode": "000", "messages": "SUCCESS", "data": { "agentId": 1234 } }
```

`data` carries the new agent id and nothing else. Keep it — it is how you refer to the agent
afterwards, including as a `parentAgentId` for its own sub-agents.

Lookup ids in the payload (`businessTypeId`, `cityId`, `occupationId`, `expectedMonthlyVolumeId`,
`accountPurposeId`) come from `GET /agentapp/v1/getAllLovs`. An unknown `businessTypeId` is
rejected outright with `Business Type Not Found`.

---

## Error handling

| `responsecode` | Meaning | What to do |
| --- | --- | --- |
| `000` | Success | Proceed |
| `406` | Invalid Token | Portal key missing or wrong |
| `111` | Validation error | `messages` names the missing field |
| `113` | Custom message | Read `messages` — covers `Wrong Mpin` and malformed JSON |
| `125` / `402` | Account / user not found | Check the account number **and** the account level |
| `127` | Wrong MPIN | Let the customer retry |
| `134` | Beneficiary title issue | Surface `messages` |
| `409` | Record not found | No matching record |
| `1031` | Account not found at the switch | Beneficiary unresolvable |

Two habits worth building in:

- **Always branch on `responsecode`, never on the HTTP status.** Business failures return HTTP 200.
- **Never log the MPIN, the portal key, or a full account number.** Mask them.

---

## Testing

A Postman collection covering every endpoint above is available from the backend team
(`DFS_Corporate_Portal.postman_collection.json`). It is distributed directly rather than committed,
so ask for the current copy. Set `portalKey` in a Postman **environment**, not in the collection, so
the key does not travel when the file is shared.

Verified on the server at deployment:

- All endpoints return `406` without the key and with a wrong key.
- `bankList` and `getbiller` return live data.
- `fundsTransferLocal` with a wrong MPIN returns `Wrong Mpin` — the full portal → transactions →
  app MPIN chain works without any login token.
- The mobile endpoints still reject the portal key.
