# ZUGFeRD Invoice API (Security-Hardened)

Secure Spring Boot service (JDK 21) that creates **CII-compliant ZUGFeRD PDF/A-3** invoices using **Mustangproject 2.26.0**.

Designed for **Endur 17 / OpenJVS (JDK 8)** and aligned with OMV SO.SA API security controls.

**Repository:** https://github.com/rkpnp1-debug/zugferd-api

## Architecture

```
Endur 17 / OpenJVS / JDK 8
        |  Crystal PDF -> secured INPUT share
        |  HTTPS POST (JWT or API-Key + invoice data + file name)
        v
ZUGFeRD Web API  (Spring Boot / JDK 21 / Mustang 2.26.0)
        |  Map -> CII XML -> embed into PDF/A-3 -> validate
        |  Write to secured OUTPUT share
        v
ZUGFeRD PDF (CII-compliant, validated)
```

## Security Controls (Application Layer)

| Control | Implementation |
|---------|----------------|
| SO.SA-1.7 | Strict DTO validation, size limits, path-traversal prevention, CORS |
| SO.SA-1.5 | JWT preferred (scope `zugferd.generate`) + API-Key fallback |
| SO.SA-1.14 | HTTPS; TLS at API Gateway |
| SO.SA-1.9 | Sanitized errors, no stack traces/paths |
| SO.SA-1.8 | Request size limits, concurrency semaphore |
| SO.SA-1.10 | `/api/v1` + OpenAPI docs |
| SO.SA-1.12 | X-Correlation-ID + structured logging |
| SO.SA-1.6 | No hardcoded secrets |
| SO.SA-1.15 / OSS | Pinned Mustang, OWASP dependency-check |

## API

```
POST /api/v1/invoices/zugferd
Content-Type: application/json
Authorization: Bearer <JWT>   # or X-API-Key
```

See source for full request/response schema.

## Build

```bash
mvn clean package -DskipTests
java -jar target/zugferd-api-1.0.0.jar
```

OpenAPI: `/swagger-ui.html`

## Config (env)

- `ZUGFERD_INPUT_ROOT` / `ZUGFERD_OUTPUT_ROOT`
- `SECURITY_JWT_ENABLED` / `SECURITY_JWT_ISSUER_URI`
- `SECURITY_API_KEY`
- `SECURITY_CORS_ORIGINS`
