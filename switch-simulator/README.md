# switch-simulator

Mock of the external payment switch (1LINK), speaking ISO 8583.

    switch-gateway  <->  switch-simulator

## Responsibility

Answer an ISO 8583 message with a well-formed ISO 8583 message. That is all.

It has no DFS business logic: it does not debit or credit anything, does not touch the DFS database
and does not invent customer data.

## Two directions

* **ISO 8583 server** on `switch.simulator.iso-port` (default 6661). The gateway connects as a
  client. Incoming `0200`/`620000` is answered with `0210`, `0220`/`480000` with `0230`, and
  `0800` network management with `0810`.
* **REST triggers** for switch-originated traffic, i.e. another bank sending funds to DFS:

  | Method | Path | Effect |
  |---|---|---|
  | POST | `/switch-simulator/title-fetch` | pushes a `0200` down the gateway session |
  | POST | `/switch-simulator/ibft-advice` | pushes a `0220` down the gateway session |
  | GET  | `/switch-simulator/disconnect` | drops the session, for link-failure testing |

## Mock rule

Echo the transaction fields back and answer with `switch.simulator.default-response-code`
(DE-39, default `00`). DE-120 is echoed too, so the beneficiary title the transaction layer resolved
from `TBL_TRANSACTION_MOCK` travels out and back unchanged rather than being fabricated here.

To simulate a decline, set `switch.simulator.default-response-code` to any DE-39 value from the
1LINK spec section 9.30, for example `68` (invalid to account) or `04` (low balance).

## Configuration

    switch.simulator.iso-port                     ISO 8583 listener port
    switch.simulator.acquirer-institution-code    DE-32
    switch.simulator.forwarding-institution-code  DE-33
    switch.simulator.card-acceptor-name / -city   DE-43 (1LINK spec 9.33)
    switch.simulator.default-response-code        DE-39 to answer with
