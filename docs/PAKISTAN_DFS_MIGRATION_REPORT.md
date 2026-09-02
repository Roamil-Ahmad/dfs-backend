# Pakistan DFS Migration — Complete Report

**Scope:** 9 Java/Spring Boot services migrated from the Afghanistan `BARKATPAY` Oracle schema
to the Pakistan `DFS` Oracle schema.
**Date:** 19 August 2026
**Pakistan DB:** `jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1` · schema `DFS` · inspected **read-only**

---

## 0. Executive summary

| Measure | Result |
|---|---|
| Services build & package | **9 / 9 SUCCESS** |
| DB-connected services boot against Pakistan DB | **7 / 7** |
| Database modifications | **0** |
| Business-logic changes | **0** |
| Regressions vs. pre-migration baseline | **0** |
| `barakatpay` occurrences remaining | **3** (2 files, both deliberate — see §7) |

### The key finding

Both schemas live on the **same** database instance, so they were diffed directly rather than
inferred. All **2,289 columns** of `DFS` were compared against `BARKATPAY`, plus every sequence,
view, package, procedure, function and trigger.

> **The entire structural delta is one rename family: Tazkira → NID.**
> Sequences (131), views (18), packages (11), procedures (2), functions (4) and triggers (111)
> all match by name. Table lists match except the two block-list tables.
> Nothing else about the Pakistan schema differs.

This is what kept the migration small and safe. It also means **anything else that fails against
the Pakistan DB was already failing against the Afghanistan DB** — a distinction that was
*verified*, not assumed, and which shapes §8 of this report.

---

## 1. DB comparison summary — every affected table

### TBL_CUSTOMER

```
Table:                          TBL_CUSTOMER
Old Java/Afghanistan Mapping:   TAZKIRA, TAZKIRA_ISSUE_DATE, TAZKIRA_EXPIRY_DATE, TAZKIRA_VERIFIED
Pakistan DB Structure:          NID_NO           VARCHAR2(200) NULL
                                NID_ISSUE_DATE   DATE          NULL
                                NID_EXPIRY_DATE  DATE          NULL
                                NID_VERIFIED     VARCHAR2(1)   NULL
                                UK: UK_CUSTOMER_TAZKIRA (NID_NO)   <- constraint name unchanged in DB
Difference:                     4 columns renamed
Java Changes Required:          @Column x3 retargeted; field `tazkira` had NO @Column (implicit
                                mapping to TAZKIRA) -> needs explicit @Column(name="NID_NO")
Java Changes Completed:         YES - AgentApp, App, ThirdParties, Transactions, backoffice
```

### TBL_AGENT

```
Table:                          TBL_AGENT
Old Java/Afghanistan Mapping:   TAZKIRA, TAZKIRA_ISSUE_DATE, TAZKIRA_EXPIRY_DATE
Pakistan DB Structure:          NID_NO           VARCHAR2(200) NULL
                                NID_ISSUE_DATE   DATE          NULL
                                NID_EXPIRY_DATE  DATE          NULL
                                UK: UK_AGENT_CNIC (NID_NO)
Difference:                     3 columns renamed
Java Changes Required:          @Column x2 retargeted; implicit `tazkira` field -> explicit @Column
Java Changes Completed:         YES - AgentApp, backoffice, Transactions, ThirdParties
```

### TBL_USER

```
Table:                          TBL_USER
Old Java/Afghanistan Mapping:   TAZKIRA (BigDecimal, implicit mapping - no @Column)
Pakistan DB Structure:          NID_NO  NUMBER(22) NOT NULL
                                UK: UK_USER_CNIC (NID_NO)
Difference:                     1 column renamed
Java Changes Required:          Field rename + explicit @Column(name="NID_NO")
Java Changes Completed:         YES - AgentApp, App, ThirdParties, Transactions, backoffice, Workflow
```

### TBL_BIOVERISYS

```
Table:                          TBL_BIOVERISYS
Old Java/Afghanistan Mapping:   TAZKIRA (implicit mapping - no @Column)
Pakistan DB Structure:          NID_NO  VARCHAR2(200) NOT NULL
Difference:                     1 column renamed
Java Changes Required:          Field rename + explicit @Column; native query column ref
Java Changes Completed:         YES - AgentApp, App, ThirdParties, Transactions
```

### TBL_TRANS_HEAD

```
Table:                          TBL_TRANS_HEAD
Old Java/Afghanistan Mapping:   DEPOSITOR_TAZKIRA
Pakistan DB Structure:          DEPOSITOR_NID  VARCHAR2(200) NULL
Difference:                     1 column renamed
Java Changes Required:          @Column retargeted
Java Changes Completed:         YES - AgentApp, App, ThirdParties, Transactions
```

### TBL_ACCOUNT_UPGRADE

```
Table:                          TBL_ACCOUNT_UPGRADE
Old Java/Afghanistan Mapping:   TAZKIRA_FRONT_ID, TAZKIRA_BACK_ID
Pakistan DB Structure:          NID_FRONT_ID  NUMBER(22) NULL  -> FK to TBL_DOCUMENT
                                NID_BACK_ID   NUMBER(22) NULL  -> FK to TBL_DOCUMENT
                                (FK constraint names still contain TAZKIRA in the DB - not referenced
                                 by Java, so no impact)
Difference:                     2 columns renamed
Java Changes Required:          @JoinColumn (AgentApp, App) and @Column (backoffice) retargeted;
                                native SQL joins in backoffice TblAccountUpgradeRepo
Java Changes Completed:         YES - AgentApp, App, backoffice
```

### TBL_ACCOUNT_DORMANT_REMOVAL

```
Table:                          TBL_ACCOUNT_DORMANT_REMOVAL
Old Java/Afghanistan Mapping:   TAZKIRA_FRONT_ID, TAZKIRA_BACK_ID
Pakistan DB Structure:          NID_FRONT_ID, NID_BACK_ID  NUMBER(22) NULL
Difference:                     2 columns renamed
Java Changes Required:          NONE - no service maps this table
Java Changes Completed:         N/A (verified unused across all 9 services)
```

### TBL_TAZKIRA_BLOCK_LIST -> TBL_NID_BLOCK_LIST

```
Table:                          TBL_TAZKIRA_BLOCK_LIST  ->  TBL_NID_BLOCK_LIST
Old Java/Afghanistan Mapping:   @Table(name="TBL_TAZKIRA_BLOCK_LIST"), column TAZKIRA,
                                sequenceName="TBL_TAZKIRA_BLOCK_LIST_SEQ"
Pakistan DB Structure:          TBL_NID_BLOCK_LIST
                                  NID_NO         NUMBER(22)    NOT NULL  (PK_TAZKIRA_BLOCK_LIST)
                                  NAME           VARCHAR2(250) NULL
                                  STATUS         VARCHAR2(1)   NULL
                                  MOBILE_NO      VARCHAR2(20)  NULL
                                  + audit columns
                                Sequence actually present: TBL_CNIC_BLOCK_LIST_SEQ
Difference:                     Table renamed, PK column renamed, AND the referenced sequence
                                never existed in either schema
Java Changes Required:          @Table, @Column, @Id, @SequenceGenerator, class + file rename
Java Changes Completed:         YES - backoffice. TblTazkiraBlockList -> TblNidBlockList;
                                sequenceName corrected to TBL_CNIC_BLOCK_LIST_SEQ
```

### TBL_TAZKIRA_BLOCK_LIST_TEMP -> TBL_NID_BLOCK_LIST_TEMP

```
Table:                          TBL_TAZKIRA_BLOCK_LIST_TEMP  ->  TBL_NID_BLOCK_LIST_TEMP
Old Java/Afghanistan Mapping:   @Table(name="TBL_TAZKIRA_BLOCK_LIST_TEMP"),
                                TAZKIRA, TAZKIRA_BLOCK_LIST_TEMP_ID,
                                sequenceName="TBL_TAZKIRA_BLOCK_LIST_TEMP_SEQ"
Pakistan DB Structure:          TBL_NID_BLOCK_LIST_TEMP
                                  NID_BLOCK_LIST_TEMP_ID  NUMBER(22)    NOT NULL (PK)
                                  NID_NO                  NUMBER(22)    NOT NULL
                                  NAME, MOBILE_NO, BATCH_NO, STATUS, COMMENTS, FILENAME, REASON
                                Sequence actually present: TBL_CNIC_BLOCK_LIST_TEMP_SEQ
Difference:                     Table renamed, 2 columns renamed, sequence never existed
Java Changes Required:          @Table, @Column, @Id, @SequenceGenerator, class + file rename
Java Changes Completed:         YES - backoffice. TblTazkiraBlockListTemp -> TblNidBlockListTemp;
                                sequenceName corrected to TBL_CNIC_BLOCK_LIST_TEMP_SEQ
```

### VW_MINI_STATEMENT / VW_TRANS_DETAIL_REPORT

```
Table (view):                   VW_MINI_STATEMENT, VW_TRANS_DETAIL_REPORT
Old Java/Afghanistan Mapping:   FROM_TAZKIRA, TO_TAZKIRA
Pakistan DB Structure:          FROM_NID_NO, TO_NID_NO  VARCHAR2(4000) NULL
Difference:                     2 columns renamed in each view
Java Changes Required:          NONE in Java - no service selects these columns
                                (VwMiniStatement entity does not map them; queries use other columns)
Java Changes Completed:         N/A in Java.
                                *** BUT: the DB-stored report SQL DOES select them - see section 8.1 ***
```

### Objects verified as IDENTICAL between the two schemas

Sequences (131), views (18), packages (11), package bodies (11), procedures (2), functions (4),
triggers (111), and all remaining 148 tables — no name or column differences.

---

## 2. Service-by-service report

### 2.1 AgentApp

```
Service Name: AgentApp
-------------------------
DB Configuration:
- Old configuration:
    spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV}
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME:BARKATPAY}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    spring.datasource.driver-class=oracle.jdbc.driver.OracleDriver
- New configuration:
    spring.datasource.url=jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1
    spring.datasource.username=DFS
    spring.datasource.password=DFS
    spring.datasource.driver-class=oracle.jdbc.driver.OracleDriver

Package:
- Old package: com.barakatpay.agentapp   (284 files)
- New package: com.dfs.agentapp
- pom groupId: com.barakatpay -> com.dfs
- Directory:   src/main/java/com/barakatpay/ -> src/main/java/com/dfs/

Tables Checked:
- TBL_CUSTOMER, TBL_AGENT, TBL_USER, TBL_BIOVERISYS, TBL_TRANS_HEAD, TBL_ACCOUNT_UPGRADE
- plus all other mapped tables (validated, no changes needed)

Entity Changes (6 entities):
- TblCustomer   : @Column TAZKIRA_EXPIRY_DATE -> NID_EXPIRY_DATE
                  @Column TAZKIRA_ISSUE_DATE  -> NID_ISSUE_DATE
                  @Column TAZKIRA_VERIFIED    -> NID_VERIFIED
                  field `tazkira` (implicit) -> `nidNo` + explicit @Column(name="NID_NO")
- TblAgent      : @Column TAZKIRA_EXPIRY_DATE -> NID_EXPIRY_DATE
                  @Column TAZKIRA_ISSUE_DATE  -> NID_ISSUE_DATE
                  field `tazkira` (implicit) -> `nidNo` + explicit @Column(name="NID_NO")
- TblUser       : field `tazkira` (implicit) -> `nidNo` + explicit @Column(name="NID_NO")
- TblBioverisy  : field `tazkira` (implicit) -> `nidNo` + explicit @Column(name="NID_NO")
- TblTransHead  : @Column DEPOSITOR_TAZKIRA -> DEPOSITOR_NID
- TblAccountUpgrade : @JoinColumn TAZKIRA_FRONT_ID -> NID_FRONT_ID
                      @JoinColumn TAZKIRA_BACK_ID  -> NID_BACK_ID
  All getters/setters renamed accordingly (getTazkira -> getNidNo, etc.)

Query Changes (11 native queries):
- TblAccountRepo   : 7 queries,  C.TAZKIRA -> C.NID_NO
- TblAgentRepo     : 2 queries,  TAZKIRA   -> NID_NO
- TblBioverisyRepo : 1 query,    tb.TAZKIRA -> tb.NID_NO
- TblCustomerRepo  : 1 query,    C.TAZKIRA -> C.NID_NO

Repository Changes:
- Derived method names renamed to match entity properties:
    findByTazkira -> findByNidNo
    findByMobileNumberOrTazkira -> findByMobileNumberOrNidNo
    findByTazkiraAndFingerIndex -> findByNidNoAndFingerIndex
  (all of these carry an explicit native @Query, so Spring Data never derived from the name)

DTO Changes:
- AgentKycRequest     : tazkiraNumber -> nidNumber, tazkiraIssuenceDate -> nidIssuenceDate,
                        tazkiraExpiryDate -> nidExpiryDate, tazkiraNumberDari -> nidNumberDari
- CustomerKycRequest  : same 4 fields
- LoginResponse       : tazkira -> nidNo ; barkatPayId -> dfsId
- TazkiraBvsRequest   -> renamed class NidBvsRequest (field tazkiraNumber -> nidNumber)

Other Changes:
- Class/file renames : TazkiraService -> NidService
                       TazkiraServiceImpl -> NidServiceImpl
                       TazkiraPostApis -> NidPostApis
                       TazkiraBvsRequest -> NidBvsRequest
- Package rename     : com.dfs.agentapp.controller.tazkira -> ...controller.nid
- Response codes     : enum constants TAZKIRA_FRONT_NOT_FOUND -> NID_FRONT_NOT_FOUND,
                       TAZKIRA_BACK_NOT_FOUND -> NID_BACK_NOT_FOUND
                       (numeric code VALUES "451"/"452" left UNCHANGED - API contract)
- Validation messages: "INVALID TAZKIRA NUMBER" -> "INVALID NID NUMBER"
- Config properties  : doc.tazkiraFront.code -> doc.nidFront.code (VALUE "TF" unchanged)
                       doc.tazkiraBack.code  -> doc.nidBack.code  (VALUE "TB" unchanged)
- Endpoint           : /getbarakatpayid/{id} -> /getdfsid/{id}
- Branding           : OpenApiConfig, JWT issuer barkatpay.com -> dfs.com
- KEPT UNCHANGED     : createMultilang(..., "TAZKIRA", ...) literal - see section 7.3

Business Logic:
- Confirmed unchanged
```

### 2.2 App

```
Service Name: App
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.barkatpay.app   (317 files)
- New package: com.dfs.app
- pom groupId: com.barkatpay -> com.dfs

Tables Checked:
- TBL_CUSTOMER, TBL_USER, TBL_BIOVERISYS, TBL_TRANS_HEAD, TBL_ACCOUNT_UPGRADE

Entity Changes (5 entities):
- TblCustomer       : 3 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblUser           : implicit -> `nidNo` + explicit @Column(name="NID_NO")
- TblBioverisy      : implicit -> `nidNo` + explicit @Column(name="NID_NO")
- TblTransHead      : @Column DEPOSITOR_TAZKIRA -> DEPOSITOR_NID
- TblAccountUpgrade : @JoinColumn TAZKIRA_FRONT_ID/TAZKIRA_BACK_ID -> NID_FRONT_ID/NID_BACK_ID

Query Changes (7 native queries):
- TblAccountRepo   : 3 queries,  C.TAZKIRA -> C.NID_NO
- TblBioverisyRepo : 1 query,    tb.TAZKIRA -> tb.NID_NO
- TblCustomerRepo  : 2 queries,  C.TAZKIRA -> C.NID_NO

Repository Changes:
- findByAccountNoOrTazkira -> findByAccountNoOrNidNo
- findByMobileNumberOrTazkira -> findByMobileNumberOrNidNo
- findByTazkiraAndFingerIndex -> findByNidNoAndFingerIndex

DTO Changes:
- CustomerKycRequest : 4 tazkira* fields -> nid*
- LoginResponse      : tazkira -> nidNo ; barkatPayId -> dfsId
- TazkiraBvsRequest  -> NidBvsRequest

Other Changes:
- Class/file renames : TazkiraService/Impl -> NidService/Impl, TazkiraPostApis -> NidPostApis
- Package rename     : com.dfs.app.controller.tazkira -> ...controller.nid
- LogSanitizer       : masked-field regex updated (cnic|nidNo|nidNumber|...) so the renamed
                       JSON keys are still redacted in logs
- Endpoint           : /v1/getbarakatpayid/{id} -> /v1/getdfsid/{id}
- Branding + JWT issuer
- KEPT UNCHANGED     : barkatpay_audit.tbl_app_user cross-schema query - see section 7.1
- KEPT UNCHANGED     : createMultilang(..., "TAZKIRA", ...) literal - see section 7.3

Business Logic:
- Confirmed unchanged
```

### 2.3 backoffice

```
Service Name: backoffice
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.barkatpay.backoffice   (268 files)
- New package: com.dfs.backoffice
- pom groupId: com.barkatpay -> com.dfs

Tables Checked:
- TBL_CUSTOMER, TBL_AGENT, TBL_USER, TBL_ACCOUNT_UPGRADE,
  TBL_NID_BLOCK_LIST, TBL_NID_BLOCK_LIST_TEMP, VW_MINI_STATEMENT, VW_TRANS_DETAIL_REPORT

Entity Changes (6 entities):
- TblCustomer            : 3 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblAgent               : 2 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblUser                : implicit -> `nidNo` + explicit @Column
- TblAccountUpgrade      : @Column TAZKIRA_FRONT_ID/BACK_ID -> NID_FRONT_ID/NID_BACK_ID
- TblTazkiraBlockList    -> TblNidBlockList
                           @Table TBL_TAZKIRA_BLOCK_LIST -> TBL_NID_BLOCK_LIST
                           @Id/@Column TAZKIRA -> NID_NO
                           @SequenceGenerator sequenceName TBL_TAZKIRA_BLOCK_LIST_SEQ
                                                        -> TBL_CNIC_BLOCK_LIST_SEQ   (see note)
                           @NamedQuery updated
- TblTazkiraBlockListTemp -> TblNidBlockListTemp
                           @Table -> TBL_NID_BLOCK_LIST_TEMP
                           @Column TAZKIRA_BLOCK_LIST_TEMP_ID -> NID_BLOCK_LIST_TEMP_ID
                           @Column TAZKIRA -> NID_NO
                           sequenceName -> TBL_CNIC_BLOCK_LIST_TEMP_SEQ

  NOTE: TBL_TAZKIRA_BLOCK_LIST_SEQ / TBL_TAZKIRA_BLOCK_LIST_TEMP_SEQ do NOT exist in either
  schema. This was a pre-existing broken reference. Because the entity had to change anyway,
  the generators now point at the sequences that actually exist.

Query Changes (~20 column references across 6 repositories):
- TblAccountRepo        : C.TAZKIRA -> C.NID_NO
- TblAccountUpgradeRepo : U.TAZKIRA_FRONT_ID/BACK_ID -> NID_FRONT_ID/NID_BACK_ID,
                          aliases TAZKIRA_FRONT/TAZKIRA_BACK -> NID_FRONT/NID_BACK
- TblAgentRepo          : 6 queries - TAZKIRA -> NID_NO, TAZKIRA_ISSUE_DATE/EXPIRY_DATE ->
                          NID_ISSUE_DATE/NID_EXPIRY_DATE, alias "AS tazkira" -> "AS nidNo",
                          SpEL :#{#searchAgent.cnic} untouched
- TblCustomerRepo       : 3 queries - C.TAZKIRA -> C.NID_NO, date columns,
                          SpEL :#{#searchCustomerRequest.tazkira} -> .nidNo
- TblUserRepo           : WHERE tazkira -> WHERE NID_NO   *** see section 6.1 ***
- TblNidBlockListRepo   : renamed from TblTazkiraBlockListRepo

Repository Changes:
- findAgentByTazkira    -> findAgentByNidNo
- findCustomerByTazkira -> findCustomerByNidNo
- TblTazkiraBlockListRepo -> TblNidBlockListRepo (interface + file)

DTO Changes (8):
- SearchCustomerRequest, SearchCustomerResponse, SearchAgentResponse, SearchKycResponse,
  AgentDetailResponse, CreateAgentAccountRequest, CreateUserRequest, UpdateCustomerRequest
  (field tazkira -> nidNo, tazkiraIssueDate/tazkiraExpiryDate -> nidIssueDate/nidExpiryDate)

Other Changes:
- Response code enum : USER_TAZKIRA_REGISTERED -> USER_NID_REGISTERED (code "144" unchanged)
- Email subjects     : "BARAKATPAY Agent Email" -> "DFS Agent Email", etc. (5 places)
- Branding + JWT issuer
- pom description updated

Business Logic:
- Confirmed unchanged
```

### 2.4 Transactions

```
Service Name: Transactions
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.wallet.transaction   (UNCHANGED - contains no barakatpay)
- New package: com.wallet.transaction
- pom groupId: com.barkatpay -> com.dfs

Tables Checked:
- TBL_CUSTOMER, TBL_AGENT, TBL_USER, TBL_BIOVERISYS, TBL_TRANS_HEAD

Entity Changes (5 entities):
- TblCustomer  : 3 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblAgent     : 2 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblUser      : implicit -> `nidNo` + explicit @Column
- TblBioverisy : implicit -> `nidNo` + explicit @Column
- TblTransHead : @Column DEPOSITOR_TAZKIRA -> DEPOSITOR_NID

Query Changes (3 native queries):
- TblAccountRepo  : AG.TAZKIRA -> AG.NID_NO ; tc.TAZKIRA -> tc.NID_NO
                    c.tazkira -> c.NID_NO   *** see section 6.1 ***
- TblCustomerRepo : WHERE TAZKIRA -> WHERE NID_NO

Repository Changes:
- findByTazkira -> findByNidNo

DTO Changes (9):
- FundTransferRequest, FundTransferCardRequest, FundTransferResponce, GetReceivedMoneyRequest,
  GetSavedBillsRequest, InitiateLocalFTRequest, RequestMoneyRequest, SaveTemplateRequest,
  UpdateReceivedMoneyRequest   (field tazkira -> nidNo)

Other Changes:
- Property   : barakatpay.bin -> dfs.bin  (value 900419 unchanged)
- Field      : barakatpayBin -> dfsBin
- Card data  : cardAcceptor.setName("BARAKATPAY") -> "DFS"  and location.setName(...) - 16 call
               sites total. *** OUTWARD-FACING - see section 9.2 ***
- Branding + JWT issuer

Business Logic:
- Confirmed unchanged
```

### 2.5 ThirdParties

```
Service Name: ThirdParties
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.barkatpay.thirdparties   (158 files)
- New package: com.dfs.thirdparties
- pom groupId: com.barkatpay -> com.dfs

Tables Checked:
- TBL_CUSTOMER, TBL_AGENT, TBL_USER, TBL_BIOVERISYS, TBL_TRANS_HEAD

Entity Changes (5 entities):
- TblCustomer  : 3 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblAgent     : 2 @Column retargeted + implicit `tazkira` -> `nidNo` + explicit @Column
- TblUser      : implicit -> `nidNo` + explicit @Column
- TblBioverisy : implicit -> `nidNo` + explicit @Column
- TblTransHead : @Column DEPOSITOR_TAZKIRA -> DEPOSITOR_NID

Query Changes:
- None - this service has no native SQL referencing the renamed columns

Repository Changes:
- None required

DTO Changes:
- None required (no DTO in this service exposes the renamed fields)

Other Changes:
- Branding + JWT issuer
- LEFT AS-IS: TblAgent @Column(name="TAZKIRA_APPLICANT") + field tazkiraApplicant.
  This column exists in NEITHER schema (pre-existing defect) - renaming it would be a guess.
  See section 8.2.

Business Logic:
- Confirmed unchanged
```

### 2.6 Workflow

```
Service Name: Workflow
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.workflow   (UNCHANGED - contains no barakatpay)
- New package: com.workflow
- pom groupId: com.workflow   (UNCHANGED)

Tables Checked:
- TBL_USER  (only table affected by the rename in this service)

Entity Changes (1 entity):
- TblUser : field `tazkira` (implicit) -> `nidNo` + explicit @Column(name="NID_NO")

Query Changes:
- None affected by the migration

Repository Changes:
- None

DTO Changes:
- None

Other Changes:
- JWT issuer barkatpay.com -> dfs.com
- OpenApiConfig branding

Business Logic:
- Confirmed unchanged
```

### 2.7 pricingandcommission

```
Service Name: pricingandcommission
-------------------------
DB Configuration:
- Old configuration: jdbc:oracle:thin:@192.168.0.203:1521/BRKTPDEV / BARKATPAY / ${env}
- New configuration: jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1 / DFS / DFS

Package:
- Old package: com.mfs.pricingprofile   (UNCHANGED)
- New package: com.mfs.pricingprofile
- pom groupId: com.legal   (UNCHANGED - not a barakatpay reference)

Tables Checked:
- Pricing / commission / charges / tax tables - none affected by the Tazkira->NID rename

Entity Changes:
- None required by the migration

Query Changes:
- None

Repository Changes:
- None

DTO Changes:
- None

Other Changes:
- JWT issuer, OpenApiConfig branding
- Stale comment "Package Name:com.barkatpay.backoffice" in LoggingAspect corrected

Business Logic:
- Confirmed unchanged

NOTE: this service carries the largest set of PRE-EXISTING entity mismatches (see section 8.2).
      None are migration-related and none were changed.
```

### 2.8 CardManagement

```
Service Name: CardManagement
-------------------------
DB Configuration:
- Old configuration: NONE - this service has no datasource
- New configuration: NONE (unchanged)

Package:
- Old package: com.barakatpay.cardapi   (31 files)
- New package: com.dfs.cardapi

Tables Checked:
- None - no database access

Entity Changes: None
Query Changes: None
Repository Changes: None
DTO Changes: None

Other Changes:
- Package rename only + OpenApiConfig branding

Business Logic:
- Confirmed unchanged
```

### 2.9 pushnotification

```
Service Name: pushnotification
-------------------------
DB Configuration:
- Old configuration: NONE - this service has no datasource
- New configuration: NONE (unchanged)

Package:
- Old package: com.notification.firbase   (UNCHANGED)
- New package: com.notification.firbase

Tables Checked:
- None - no database access

Entity Changes: None
Query Changes: None
Repository Changes: None
DTO Changes: None

Other Changes:
- Notification title "Barakatpay" -> "DFS"
- OpenApiConfig branding
- KEPT UNCHANGED: Firebase service-account JSON and its filename - see section 7.2

Business Logic:
- Confirmed unchanged
```

---

## 3. Database configuration — final state

Applied to the **7** services that have a datasource (CardManagement and pushnotification have none):

```properties
spring.datasource.url=jdbc:oracle:thin:@167.233.72.195:1521/ORCLPDB1
spring.datasource.username=DFS
spring.datasource.password=DFS
spring.datasource.driver-class=oracle.jdbc.driver.OracleDriver
```

| Service | File | Status |
|---|---|---|
| AgentApp | `src/main/resources/application.properties` | Updated |
| App | `src/main/resources/application.properties` | Updated |
| ThirdParties | `src/main/resources/application.properties` | Updated |
| Transactions | `src/main/resources/application.properties` | Updated |
| Workflow | `src/main/resources/application.properties` | Updated |
| backoffice | `src/main/resources/application.properties` | Updated |
| pricingandcommission | `src/main/resources/application.properties` | Updated |
| CardManagement | — | No datasource |
| pushnotification | — | No datasource |

No `application.yml`, `bootstrap.*`, or profile-specific config files exist in any service.
No custom `DataSource` @Configuration classes exist.

As requested, the previous `${SPRING_DATASOURCE_URL:...}` environment-variable indirection was
**removed** in favour of literal values. See section 9.5 for the security implication.

---

## 4. Package rename — `barakatpay` / `barkatpay` -> `dfs`

Both spellings were present in the codebase and both were migrated.

| Service | Old package | New package | Files |
|---|---|---|---|
| AgentApp | `com.barakatpay.agentapp` | `com.dfs.agentapp` | 284 |
| App | `com.barkatpay.app` | `com.dfs.app` | 317 |
| backoffice | `com.barkatpay.backoffice` | `com.dfs.backoffice` | 268 |
| ThirdParties | `com.barkatpay.thirdparties` | `com.dfs.thirdparties` | 158 |
| CardManagement | `com.barakatpay.cardapi` | `com.dfs.cardapi` | 31 |
| Transactions | `com.wallet.transaction` | unchanged | — |
| Workflow | `com.workflow` | unchanged | — |
| pricingandcommission | `com.mfs.pricingprofile` | unchanged | — |
| pushnotification | `com.notification.firbase` | unchanged | — |

**Total files with package references rewritten: 1,059**

What was updated:
- Physical directories moved (`src/main/java/com/barakatpay/` -> `src/main/java/com/dfs/`)
- `package` declarations and all `import` statements
- Maven `groupId` in 5 poms (`com.barakatpay` / `com.barkatpay` -> `com.dfs`)
- pom `<description>` fields
- OpenAPI titles / descriptions / contact names
- JWT issuer constant `barkatpay.com` -> `dfs.com` (all 7 services that define it)
- API endpoint `/getbarakatpayid/{id}` -> `/getdfsid/{id}`; method `getbarakatpayid` -> `getdfsid`
- DTO field `barkatPayId` -> `dfsId` (+ getter/setter)
- Property `barakatpay.bin` -> `dfs.bin`; field `barakatpayBin` -> `dfsBin`
- Email subjects and push-notification title
- GitHub Actions deploy paths `/var/www/barakatpay/` -> `/var/www/dfs/`
- README.md, OPENAPI-AND-TRACING-SETUP.md, ENHANCEMENTS.md

There were **no test sources** in any of the 9 services, so no test packages required updating.
There is no Gradle build in any service.

**Not touched (correctly):**
- `.git/` internals — modifying these would corrupt the repositories. The stale paths clear
  themselves once the renames are committed.
- `.idea/` — IDE-local state, gitignored in every service, regenerated automatically.

---

## 5. Verification performed

Compilation alone cannot catch a wrong column name inside a SQL string, so verification was
done against the **live Pakistan database** at several levels.

| Check | Method | Result |
|---|---|---|
| Entity mappings | Extracted all 5,038 `@Table`/`@Column`/`@JoinColumn` pairs; checked each against `ALL_TAB_COLUMNS` | 35 broken -> **0** |
| Entity mappings, executed | Built `SELECT <every mapped column> FROM <table>` for all 565 entity/table pairs; Oracle parsed each server-side | **530 pass**, 35 pre-existing |
| Native SQL | Extracted all 231 native `@Query` strings, reassembled the Java string concatenations, had Oracle describe each (parse only — never executed) | **221 pass**, 10 pre-existing |
| Regression control | Ran the same entity check on the **pre-change** sources against `BARKATPAY`, diffed the failure sets | **Byte-identical** |
| Compilation | `mvn clean package -DskipTests` on all 9 services (JDK 17) | **9/9 BUILD SUCCESS** |
| Runtime | Booted all 7 DB-connected services against the Pakistan DB | **7/7 started** |
| Data | Executed renamed queries for real against `DFS` | Rows returned |

### The regression control is the most important line

The set of entity mappings that fail is **byte-for-byte the same before and after** the migration
— same 35 entries, same tables. That is direct evidence that:

1. the migration introduced **no regressions**, and
2. it did **not** paper over anything that was already broken.

### Runtime boot results

| Service | Result |
|---|---|
| backoffice | Started — `Started BackofficeApplication in 36.2s` |
| App | Started — `Started AppApplication in 46.2s` |
| Workflow | Started — `Started WorkflowApplication in 25.0s` |
| pricingandcommission | Started — `Started PricingprofileApplication in 26.7s` |
| AgentApp | Started — `Started AgentappApplication in 36.0s` * |
| ThirdParties | Started — `Started ThirdpartiesApplication in 29.9s` * |
| Transactions | Started — `Started TransactionApplication in 36.8s` * |

\* These three initially failed on **missing deployment secrets** (`AGENTAPP_PORTAL_TOKEN`,
`TWILIO_ACCOUNT_SID`, client secrets) — unrelated to the database. In both cases HikariCP had
already connected to the Pakistan DB successfully. Re-run with dummy values, all three started
cleanly, confirming Hibernate mapped every entity and Spring Data resolved every repository.

---

## 6. Defects found and fixed during verification

### 6.1 Two regressions introduced by the mechanical rename — CAUGHT AND FIXED

Parsing the SQL against Oracle found two places where a **lowercase `tazkira` was a SQL column
reference**, not a Java identifier, so the rename had produced `nidNo` instead of `NID_NO`:

| File | Before | After |
|---|---|---|
| `Transactions/.../repo/TblAccountRepo.java` | `AND c.nidNo = :cnic` | `AND c.NID_NO = :cnic` |
| `backoffice/.../repo/TblUserRepo.java` | `WHERE nidNo = :cnic` | `WHERE NID_NO = :cnic` |

**Neither would have failed compilation or application startup.** They would have surfaced as
`ORA-00904: invalid identifier` at runtime, in production, on the agent-account-by-CNIC lookup
and the user-by-CNIC lookup respectively. Both were fixed and re-verified.

### 6.2 Broken sequence reference — FIXED

`backoffice` block-list entities referenced `TBL_TAZKIRA_BLOCK_LIST_SEQ` /
`TBL_TAZKIRA_BLOCK_LIST_TEMP_SEQ`, which have **never existed in either schema**. The real
sequences are `TBL_CNIC_BLOCK_LIST_SEQ` / `TBL_CNIC_BLOCK_LIST_TEMP_SEQ`. Without this fix the
entities would have been renamed correctly and still failed on insert.

---

## 7. Deliberate exceptions — `barakatpay` references intentionally kept

**3 occurrences remain, in 2 files.** Each would break something real if renamed.

### 7.1 `App/.../repo/TblAppUserRepo.java` — `barkatpay_audit.tbl_app_user`

```java
@Query(value = "SELECT COUNT(*) FROM barkatpay_audit.tbl_app_user " + ...
```

There is **no `DFS_AUDIT` schema** on the Pakistan instance. `BARKATPAY_AUDIT` is the only audit
schema present, and the `DFS` user can read it (verified: 1,562 rows in `TBL_APP_USER`).
Renaming this to `dfs_audit` would fail with `ORA-00942: table or view does not exist`.

Per the governing rule — *the Pakistan DB is the source of truth; change Java to match the DB, not
the DB to match Java* — this reference stays until the DB team provisions a renamed audit schema.

### 7.2 `pushnotification` — Firebase service-account credential

- `src/main/resources/barakatpay-2e4d4-firebase-adminsdk-fbsvc-0c5b7fb862.json`
- referenced by `FirebaseConfig.java`

The JSON's `project_id` and `client_email` identify the **real Google Firebase project**.
Rewriting them produces a credential that authenticates against nothing and silently kills push
notifications. This needs a new Pakistan Firebase project to be provisioned — not a
find-and-replace. Left byte-identical, as agreed.

### 7.3 Also left alone on purpose (not `barakatpay`, but `TAZKIRA`)

**The `"TAZKIRA"` literal written into `TBL_MULTILANGUAGE.COLUMN_NAME`** (3 call sites:
AgentApp `CustomerSignUpServiceImpl` + `SignUpServiceImpl`, App `SignUpServiceImpl`).

This is a **data value**, not a schema reference. **47 existing rows** already carry
`COLUMN_NAME = 'TAZKIRA'`. Changing the literal would split new rows from old with no way to
migrate the existing data under the read-only rule. The only read path in these services filters
on `LANGUAGE_ID` + `APP_SCREEN_NAME`, never on `COLUMN_NAME`, so nothing breaks either way.

**`TBL_AGENT.TAZKIRA_APPLICANT` in ThirdParties** — the column exists in neither schema, so any
rename would be a guess. Reported in section 8.2 instead.

---

## 8. Open items for your team

None of these were introduced by the migration. None are fixable from the Java side alone.

### 8.1 Requires a database change (DB team)

**The Transaction Detail Report will fail.**
`DFS.TBL_REPORT_QUERY` stores report SQL as **data**, and the row named *"Transaction Detail
Report"* still selects `FROM_TAZKIRA` / `TO_TAZKIRA` — columns that `VW_TRANS_DETAIL_REPORT` no
longer has (they are now `FROM_NID_NO` / `TO_NID_NO`). `backoffice/ReportsServicempl.java:106`
reads `REPORT_QUERY_SAMPLE` and executes it verbatim.

This needs an `UPDATE` to that row. It could not be made under the read-only rule.

**101 of 111 `DFS` triggers are INVALID.**
Cause: the audit types (`T_VARRAY`, `T_TABLE_NAME`, `T_COLUMN_NAME`, `T_OLD_VALUE_DB`, …) that
exist in `BARKATPAY` were not created in `DFS`, and the trigger bodies reference
`BARKATPAY_AUDIT` sequences the `DFS` user cannot see. Additionally, **4 DFS-owned triggers still
fire on `BARKATPAY.*` tables** rather than `DFS.*` (e.g. `TRG_CUSTOMER_BUD` is declared
`BEFORE UPDATE OR DELETE ON BARKATPAY.TBL_CUSTOMER`).

Impact today: almost all are DISABLED, so they do not block DML. The single ENABLED-and-INVALID
trigger is `TRG_DISPUTE_REQUEST_AUDIT` on `TBL_DISPUTE_REQUEST` — **no service in these 9 touches
that table**, so there is no runtime impact. But **audit capture is effectively off.**

**`LKP_BANK` still contains only Afghan banks** (Pashtany, Ghazanfar, Azizi, Kabul, DAB, …,
plus `BRKT | BarakatPay`). Separately, the config value `bank.code=BKTY` **matches no row in
either schema** — it is consumed at 4 KYC/onboarding sites via `@Value("${bank.code}")`. The
correct Pakistan bank code needs confirming.

### 8.2 Pre-existing Java/DB mismatches (NOT migration-related)

These fail **identically** against the Afghanistan schema — proven by the regression control in
section 5. They were left alone to keep the migration scope clean, but they are still open work.

**Entities mapped to tables that exist in neither schema:**
- `TBL_NADRA`, `TBL_NADRA_MISMATCH`, `TBL_NADRA_MOCK` — AgentApp, App, ThirdParties, Transactions
- `TBL_CNIC_BLOCK_LIST`, `TBL_CNIC_BLOCK_LIST_TEMP` — same 4 services
- `TBL_ALERT_DETAIL`, `TBL_MC_REQUEST_DETAIL` — Workflow

**Columns mapped that do not exist:**
- `TBL_USER.MOTHER_NAME` — AgentApp, App, ThirdParties, Transactions
- `TBL_CUSTOMER.SOURCE_OF_INCOME` — ThirdParties
- `TBL_AGENT.{ROLE_ID, SUB_AGENT_OF, NAME_OF_APPLICANT, MOBILE_APPLICANT, PHONE_APPLICANT,
  TAZKIRA_APPLICANT}` — ThirdParties
- `TBL_AUTH_ACCESS_TOKEN.{USER_ID, CUSTOMER_ID, AGENT_ID, MW_CHANNEL_ID}` — ThirdParties
- `pricingandcommission` — `TblCustomer`, `TblAgent`, `TblAccount`, `TblCustomerAll`,
  `TblMessage`, `LkpMessageType` map a large set of non-existent columns (`LKP_*_ID`, `CNIC_*`,
  `APPROVED_YN`, `US_RESIDENCE`, `CELL_NO_PAKISTAN`, `EXPECTED_MONTHLY_CREDIT`, …). These look
  like leftovers from a *different* Pakistan-flavoured schema and are worth a dedicated review.

**Native queries that do not parse (10 of 231):**
- `Workflow/TblMcRequestRepo` (4 queries) — uses `MC_REQUEST`, `MC_PENDING_REQUEST`,
  `MC_REQUEST_ACTION`, `UMGT_USER`. Real names carry a `TBL_` prefix
  (`TBL_MC_REQUEST`, `TBL_MC_PENDING_REQUEST`, `TBL_MC_REQUEST_ACTION`); there is **no
  `UMGT_USER` at all**.
- `Workflow/TblAlertDetailRepo` — `TBL_ALERT_DETAIL` does not exist.
- `TblCustomerAllRepo` (AgentApp, App, ThirdParties) — filters `a.tmsverified`, no such column.
- `backoffice/TblOtpRepo` — contains the literal text `AND TblOtpRepo = :appUserId`
  (the repository class name pasted into the SQL).
- `AgentApp/TblAgentRepo` — `SELECT * FROM TBL_AGENT NID_NO =:nidNo` is **missing its `WHERE`**.

### 8.3 Deployment / environment — please confirm

**Deploy paths moved.** All 8 GitHub Actions workflows changed from `/var/www/barakatpay/...` to
`/var/www/dfs/...`, including the `--env-file /var/www/dfs/.env` reference.
**The directory and its `.env` file must be relocated on the server before the next deploy**, or
the containers will start without configuration. Docker container/network names (`barakat-app`,
`barakat-net`) were **left as-is** — tell me if you want those renamed too.
(AgentApp has no deploy workflow.)

**Card-acceptor name is now `"DFS"`** where it was `"BARAKATPAY"` in messages sent to the card
processor (16 call sites in Transactions, plus `CardLinkingServiceImpl`). This is **outward-facing
data** — worth confirming with the processor so reconciliation does not break.

**JWT issuer changed** from `barkatpay.com` to `dfs.com`, consistently across all services.
Tokens issued before the cutover will not validate after it — **deploy the services together.**

**Geo data is still Afghan.** Transactions reads `gadm41_AFG_1.json` / `gadm41_AFG_2.json` from
`location.gadm.directory` for province/district reverse-geocoding. Pakistan equivalents
(`gadm41_PAK_*`) need to be supplied. The filenames were **not** changed because the replacement
files do not exist yet — changing them blindly would break the feature immediately.

**Credentials are now hard-coded** in `application.properties` as requested, replacing the
`${SPRING_DATASOURCE_*}` indirection. This puts the DB password in the repository and removes
per-environment override. Worth revisiting before production.

**API contract changed** (as approved). Renamed JSON keys: `tazkiraNumber` -> `nidNumber`,
`tazkiraIssuenceDate` -> `nidIssuenceDate`, `tazkiraExpiryDate` -> `nidExpiryDate`,
`tazkiraNumberDari` -> `nidNumberDari`, `tazkira` -> `nidNo`, `barkatPayId` -> `dfsId`.
Renamed endpoints: `/getbarakatpayid/{id}` -> `/getdfsid/{id}`.
**All mobile/portal clients must be updated in lockstep.**

---

## 9. Confirmation: database was NOT modified

The instruction was absolute, so it was enforced **mechanically**, not by care alone.

1. All database access went through a purpose-built wrapper that:
   - **refuses any statement** not beginning with `SELECT` or `WITH`;
   - rejects any statement containing `INSERT`, `UPDATE`, `DELETE`, `DROP`, `ALTER`, `CREATE`,
     `TRUNCATE`, `MERGE`, `GRANT`, `REVOKE`, `COMMIT`, `ROLLBACK`, `EXECUTE`, `CALL`;
   - opens the connection with `setReadOnly(true)` and `setAutoCommit(false)`;
   - issues `rollback()` on every connection before closing.
2. Query validation used Oracle's **describe** path (`PreparedStatement.getMetaData()`) —
   statements were **parsed, never executed**.
3. `DFS` object counts are **identical** to the start of the session:

   | Object type | Count |
   |---|---|
   | TABLE | 150 |
   | INDEX | 202 |
   | SEQUENCE | 131 |
   | TRIGGER | 111 |
   | VIEW | 18 |
   | PACKAGE / PACKAGE BODY | 11 / 11 |
   | FUNCTION | 4 |
   | PROCEDURE | 2 |
   | TYPE | 5 |
   | LOB | 14 |

4. The latest `LAST_DDL_TIME` across all `DFS` objects was **2026-08-19 13:36:46**, roughly three
   hours before this session's work — i.e. no DDL was executed.

**No table was created, altered, renamed or dropped. No column, constraint, index, trigger,
procedure, function or view was modified. No data was inserted, updated or deleted.**

---

## 10. Confirmation: business logic was NOT changed

No change was made to business rules, validation rules, processing flow, API behaviour,
decision-making logic, calculations, transaction flow, or error-handling logic.

Everything done falls into exactly five categories:

1. **Persistence mapping** — `@Table`, `@Column`, `@JoinColumn`, `@Id`, `@SequenceGenerator`
2. **SQL text** — column and table identifiers inside native queries
3. **Identifier renaming** — fields, getters/setters, class and file names, DTO fields
   (approved to extend to the API contract)
4. **Configuration** — datasource properties, property keys, branding strings
5. **Package naming** — `barakatpay`/`barkatpay` -> `dfs`

Specifically preserved:
- Response **code values** (`"451"`, `"452"`, `"144"`) — only the enum constant names changed
- Document type **code values** (`TF`, `TB`, `SOI`, `POA`, `SF`) — only property key names changed
- The `dfs.bin` value `900419`
- All conditional logic, null checks, encryption calls (`aeSencryption.encryptwith256`),
  transaction boundaries and exception types

---

## 11. Totals

```
Total Services Updated:              9
Files with package refs rewritten:   1,059
Files touched by Tazkira->NID:       109
Total Entities Updated:              27 entity classes
Total Column Mappings Corrected:     56  (35 annotation-level + 21 implicit -> explicit)
Total Repositories Changed:          15
Total Queries Updated:               ~45 column references across 231 validated native queries
Total DTOs Updated:                  24
Classes / files renamed:             11
Total Package References Updated:    1,059 files + 5 pom groupIds
barakatpay Occurrences Remaining:    3  (2 files - both documented in section 7)
DB Modifications:                    0
Business Logic Changes:              0
Regressions vs baseline:             0
Build Status:                        9/9 BUILD SUCCESS
Runtime Status:                      7/7 DB-connected services boot on Pakistan DB
```

---

## 12. Final verification checklist

**Database**
- [x] Pakistan DFS DB inspected (150 tables, 2,289 columns, all object types)
- [x] No DB structure modified
- [x] No DB data modified
- [x] All required Java changes made according to the existing DB

**DB Configuration**
- [x] All 9 services checked for old DB configuration
- [x] Old configuration replaced in all 7 services that have a datasource
- [x] Pakistan DB URL configured correctly
- [x] Username is `DFS`
- [x] Password is `DFS`
- [x] Oracle driver configuration correct

**Package Rename**
- [x] All `barakatpay` / `barkatpay` Java packages renamed to `dfs`
- [x] Folder structure matches package declarations
- [x] Imports updated
- [x] Component / entity / repository scanning verified (all default to the app package — no
      explicit `scanBasePackages`, `@ComponentScan`, `@EntityScan` or `@EnableJpaRepositories`
      overrides existed, and every service boots)
- [x] Tests updated — N/A, no test sources exist
- [x] Maven references checked (5 groupIds updated); no Gradle build present
- [x] Full codebase search performed — 3 documented exceptions remain

**DB Structure**
- [x] Entities match Pakistan DB
- [x] Column mappings match Pakistan DB
- [x] Table mappings match Pakistan DB
- [x] Getters/setters updated where fields were renamed
- [x] Repository queries updated
- [x] Native SQL updated and validated against the live DB
- [x] JPQL — verified none referenced the renamed properties
- [x] JDBC — verified no direct JDBC code exists
- [x] Projections / result mappings updated (`Object[]` positional, plus alias `AS nidNo`)
- [x] Specifications / Criteria API / QueryDSL — verified none present
- [x] Stored procedure / function calls — verified none invoked from Java
- [x] DTOs changed where genuinely required

**Business Logic**
- [x] No business logic changes
- [x] No unrelated refactoring
- [x] Existing functionality preserved

---

## 13. Backup

Pre-change sources for all 9 services (`src/` + `pom.xml`) were backed up before any edit to:

```
C:\Users\MUHAMM~1.KAS\AppData\Local\Temp\claude\d--Legal-DFS-PROJECT\
  758ab49b-1851-41ec-9978-c7156c2fcbee\scratchpad\backup\
```

This is a session-scoped temp directory — copy it somewhere durable if you want to keep it.
