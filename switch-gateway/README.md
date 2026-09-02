# switch-gateway

Thin integration layer between the DFS transaction layer and the payment switch (1LINK).

    Transaction Layer  ->  switch-gateway  ->  switch-simulator (mock 1LINK)

## Responsibility

Receive a request, turn it into an ISO 8583 message, send it to the switch, wait for the correlated
answer, map it back. Nothing else.

It holds no transaction state and takes no business decision: no balance check, no beneficiary
validation, no posting, no duplicate ruling. Those belong to the transaction layer and to the
`PKG_MW` database procedures.

## Endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/switch-gateway/outgoing/title-fetch` | 0200/0210, processing code 620000 (1LINK spec 11.13) |
| POST | `/switch-gateway/outgoing/advice` | 0220/0230, processing code 480000 (1LINK spec 11.10) |
| POST | `/switch-gateway/outgoing/sign-in` | 0800/0810 network management (1LINK spec 11.27) |
| POST | `/switch-gateway/outgoing/sign-off` | 0800/0810 network management |
| POST | `/switch-gateway/outgoing/echo` | 0800/0810 network management |

Switch-originated (incoming) traffic is not exposed here: it arrives on the ISO 8583 socket and is
handed straight to the transaction layer's incoming IBFT API.

## Deliberately absent

Do not reintroduce these; they belonged to the previous architecture:

* retry of any kind - a failure is reported, never replayed
* store-and-forward / `TBL_SWITCH_SAF`
* ActiveMQ and the in-memory queue framework
* Elasticsearch/Kafka logging over HTTP - use `org.slf4j.Logger`
* an outbound HTTP proxy - every service runs on the same host
* JPA and a datasource - the `TBL_SWITCH_*` schema does not exist in DFS

`ReconnectionFilter` is the one thing that looks like a retry and is not: it re-establishes a
dropped TCP session and never resends a transaction.

## Configuration

All technical values live in `application.properties`. Transaction data never does.

    switch.host / switch.port                     the simulator endpoint
    switch.response-timeout-ms                    how long a caller waits for DE-39
    transaction.layer.incoming.*.url              where incoming traffic is handed over
    onelink.*                                     fixed institution-level 1LINK parameters

## Message formats

`1LINK ISO8583 Message Format - Data Element Definitions v7.0` is the source of truth.
`utils/RecordDataLayout` encodes the DE-120 layout from section 9.62.1.1 and is the only place the
sub-field offsets are written down.
