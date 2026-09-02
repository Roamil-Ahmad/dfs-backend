# KYC document upload — moving from base64 JSON to `multipart/form-data`

**For:** mobile app team
**Applies to:** `POST /agentapp/v1/agentkyc` and `POST /app/v1/customerKyc`
**Status:** the new format is live alongside the old one. **The existing JSON request still works** — nothing breaks the day this ships.

---

## Why this changed

Documents were sent as base64 strings inside the JSON body. When an agent submitted several
documents at once, the request failed with:

```
unexpected end of stream on http://46.225.160.93:18001/...
```

That error is misleading. Nothing was wrong with the JSON. The service was being **killed
mid-request** and the socket closed before any response was written, so the HTTP client had
nothing to report but a broken stream.

The cause was memory. A base64 payload is held several times over at once:

| Stage | Cost for `S` bytes of documents |
| --- | --- |
| Raw request body buffered for logging | `S` |
| Parsed into Java strings | `2S` — Java strings are UTF-16, 2 bytes per character |
| Re-serialised by the logging layer | `2S` |
| String concatenation while logging | more |
| Base64 decoded back into bytes | `0.75S` per document |

Roughly **ten times the file size**, live in memory simultaneously — and base64 encoding has
already added 33% before any of that. A few megabytes of documents was enough to exceed the
container's memory limit.

With `multipart/form-data` each file is streamed **straight to disk** and read once. Memory stays
flat regardless of how many documents are attached, and the upload is about a third smaller on the
wire because there is no base64 expansion.

---

## What changes for the app

Same URL, same method, same response. Only the request encoding differs.

Send `Content-Type: multipart/form-data` with:

| Part name | Type | Required | Contents |
| --- | --- | --- | --- |
| `request` | text | yes | The **existing** JSON envelope, unchanged, except `payload.documents` is removed |
| `documents` | file | no | One part **per document**. Repeat the part name for each file. |

Two details worth reading twice:

- The part is named `documents` and is **repeated** — not `documents[0]`, `documents[1]`, and not a
  single part containing an array.
- The `request` part is read as plain text, so you **do not need to set a `Content-Type` on it**.
  This is deliberate: several mobile HTTP libraries make that awkward, and getting it wrong is a
  common source of `415 Unsupported Media Type`.

Headers (`Authorization`, and whatever your build already sends) are unchanged. Do not set the
`Content-Type` header manually on the overall request — let your HTTP library set it, because it
has to append the multipart `boundary`.

### The `request` part

Exactly what you send today, minus the documents:

```json
{
  "channel": "AGNT",
  "imieNo": "02525487485485",
  "latitude": "",
  "logitude": null,
  "language": "",
  "payload": {
    "mobileNumber": "03001234567",
    "nidNo": "3520112345671",
    "fullName": "…",
    "levelCode": "…"
  }
}
```

**Remove `payload.documents` entirely.** If you leave it in, it is ignored — the uploaded file
parts take priority — but it defeats the purpose, since the base64 still has to be parsed.

### File name and content type

These now come from the multipart part itself rather than the `docFileName` / `contentType` fields
of the old JSON:

- **filename** → set it on the part; it becomes the stored document name
- **content type** → set it per part (`image/jpeg`, `image/png`, `application/pdf`)

> One thing to correct while you are here: payloads we received previously declared
> `"contentType": "image/png"` while the bytes were actually **JPEG**. Please set the content type
> to what the file genuinely is.

---

## Examples

### curl (for testing)

```bash
curl -X POST 'http://46.225.160.93:18001/agentapp/v1/agentkyc' \
  -H 'Authorization: Bearer <token>' \
  -F 'request={"channel":"AGNT","imieNo":"02525487485485","payload":{"mobileNumber":"03001234567","nidNo":"3520112345671"}}' \
  -F 'documents=@/path/nid-front.jpg;type=image/jpeg' \
  -F 'documents=@/path/nid-back.jpg;type=image/jpeg' \
  -F 'documents=@/path/selfie.jpg;type=image/jpeg'
```

### Android — Kotlin / OkHttp

```kotlin
val body = MultipartBody.Builder()
    .setType(MultipartBody.FORM)
    .addFormDataPart("request", envelopeJson)          // no content type needed
    .addFormDataPart(
        "documents", "nid-front.jpg",
        File(nidFrontPath).asRequestBody("image/jpeg".toMediaType())
    )
    .addFormDataPart(
        "documents", "nid-back.jpg",
        File(nidBackPath).asRequestBody("image/jpeg".toMediaType())
    )
    .build()

val request = Request.Builder()
    .url("$BASE_URL/agentapp/v1/agentkyc")
    .addHeader("Authorization", "Bearer $token")
    .post(body)                                        // OkHttp sets Content-Type + boundary
    .build()
```

Note `addFormDataPart("documents", …)` is called once per file, with the **same** part name.

### iOS — Swift

```swift
var body = Data()
let boundary = "Boundary-\(UUID().uuidString)"

// the envelope
body.append("--\(boundary)\r\n".data(using: .utf8)!)
body.append("Content-Disposition: form-data; name=\"request\"\r\n\r\n".data(using: .utf8)!)
body.append(envelopeJson.data(using: .utf8)!)
body.append("\r\n".data(using: .utf8)!)

// one block like this per document
for doc in documents {
    body.append("--\(boundary)\r\n".data(using: .utf8)!)
    body.append("Content-Disposition: form-data; name=\"documents\"; filename=\"\(doc.name)\"\r\n"
        .data(using: .utf8)!)
    body.append("Content-Type: \(doc.mimeType)\r\n\r\n".data(using: .utf8)!)
    body.append(doc.data)
    body.append("\r\n".data(using: .utf8)!)
}
body.append("--\(boundary)--\r\n".data(using: .utf8)!)

var request = URLRequest(url: url)
request.httpMethod = "POST"
request.setValue("multipart/form-data; boundary=\(boundary)",
                 forHTTPHeaderField: "Content-Type")
request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
```

On iOS prefer `URLSession.uploadTask(with:fromFile:)` over building the body in `Data` — otherwise
you rebuild the same memory problem on the phone.

### Flutter / Dart

```dart
final request = http.MultipartRequest('POST', uri)
  ..headers['Authorization'] = 'Bearer $token'
  ..fields['request'] = envelopeJson;

for (final doc in documents) {
  request.files.add(await http.MultipartFile.fromPath(
    'documents', doc.path, contentType: MediaType('image', 'jpeg'),
  ));
}
```

---

## Response

**Unchanged.** Same envelope, same response codes, same `data` object as the JSON endpoint. No
parsing changes on your side.

```json
{ "responsecode": "000", "data": { }, "messages": "SUCCESS" }
```

---

## Limits

| | `customerKyc` (app) | `agentkyc` (agentapp) |
| --- | --- | --- |
| Max per file | 10 MB | 100 MB |
| Max per request | 50 MB | 100 MB |

Exceeding these returns an error response rather than a dropped connection.

---

## Migration notes

- **No coordinated release required.** Both formats are accepted on the same URL; the server picks
  the handler from the request's `Content-Type`. Ship the app update whenever you are ready.
- **Please migrate rather than staying on base64.** The old path has the memory cost described
  above and stays vulnerable to the same failure under load.
- Compressing/resizing images before upload is still worth doing — it is now purely a bandwidth and
  upload-time question, not a stability one.

### If something fails

| Symptom | Likely cause |
| --- | --- |
| `415 Unsupported Media Type` | `Content-Type` set manually without a `boundary` — let the library set it |
| `400`, complaining about a missing part | The envelope part is not named exactly `request` |
| Documents silently absent | File parts not all named exactly `documents`, or named `documents[0]` |
| Still `unexpected end of stream` | Request exceeds the size limits above — report it, do not retry in a loop |

---

## Server-side changes (for reference)

- New `multipart/form-data` handler on both endpoints; the JSON handler is untouched.
- Uploaded parts bypass base64 decoding entirely and are passed straight to the existing document
  pipeline, which already worked in terms of files.
- Multipart parts stream to disk (`file-size-threshold=0`) instead of being buffered in the heap.
- The logging layer now records `{fileName, contentType, bytes}` for uploaded files instead of
  serialising their contents. That removes the largest remaining memory cost — and stops customer
  identity documents from being written into log files, which they previously were.
