# DrumHub Backend

Spring Boot backend for DrumHub — a groove sharing and beat editor platform.

**Architecture**: Modular monolith, package-by-feature, single deployable JAR.  
**Stack**: Spring Boot 3.4.x · Java 21 · Maven · Spring Data JPA · Spring Security · Flyway · H2 (dev) · PostgreSQL (prod)

---

## Running locally

```bash
# Dev profile (H2 in-memory, auto-configured)
mvn spring-boot:run

# Explicit profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Profiles

| Profile | Database | Notes |
|---------|----------|-------|
| `dev`   | H2 in-memory | Default. No extra config needed. |
| `prod`  | PostgreSQL | Set `DB_URL`, `DB_USER`, `DB_PASSWORD` env vars. See `application-prod.yml`. |

## Useful endpoints (dev)

| Endpoint | Description |
|----------|-------------|
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/v3/api-docs` | OpenAPI JSON |
| `http://localhost:8080/h2-console` | H2 web console (dev only) |

H2 console settings: JDBC URL `jdbc:h2:mem:drumhub`, user `sa`, no password.

## Running tests

```bash
mvn test
```

Tests run against the `dev` profile (H2 + Flyway with empty migrations folder).

## Module roadmap

Modules are added as independent feature slices (package-by-feature). None are implemented yet in this skeleton:

- `user` — registration, login, profile management
- `genre` — music genre catalog
- `groove` — groove creation, editing, publishing
- `notification` — in-app and email notifications
- `subscription` — plans and billing
- `export` — groove export (MIDI, PDF, etc.)
- `catalog` — public groove catalog and search

Each module lives under `com.drumhub.<module>` and brings its own controllers, services, repositories, and Flyway migrations.
