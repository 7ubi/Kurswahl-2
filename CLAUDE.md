# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Kurswahl 2 is a course-selection system ("Kurswahlsystem") for the Qualifikationsphase of a German Oberstufe. It is a monorepo:

- `kurswahl/`: Spring Boot 3 backend (Java 21, Maven, JPA/MySQL, JWT auth)
- `kurswahl-ui/`: Angular 21 frontend (standalone components, Angular Material)
- `Writerside/`: JetBrains Writerside user/developer documentation (German), published to documentation.kurswahl.7ubi.de. It includes `api/api-docs.yaml` and a PlantUML DB diagram in `topics/Datenbank-Aufbau.md`.
- `docker-compose.yml`: production stack (MySQL 5.7, backend on host port 8008, nginx frontend on 4300)

The domain language, user-facing strings and error messages are German (`common/error/ErrorMessage.java`). Glossary: Tape = Band (course slot/block), Class = Kurs, StudentClass = Klasse, SubjectArea = Fachbereich, Lesson = Stunde (timeslot of a tape), Choice = Wahl (students make a 1st and 2nd choice).

## Commands

Backend (run from `kurswahl/`):
```bash
./mvnw spring-boot:run                    # needs MySQL at ${MYSQL_HOST:localhost}:3306, db kurswahldb (see application.properties)
./mvnw test                               # all tests (in-memory H2, no MySQL needed)
./mvnw test -Dtest=SubjectCreationServiceTest             # single test class
./mvnw test -Dtest=SubjectCreationServiceTest#methodName  # single test method
./mvnw -B package jacoco:prepare-agent jacoco:report      # what CI runs
```

Frontend (run from `kurswahl-ui/`):
```bash
npm start        # ng serve on :4200; proxies /api to http://localhost:8080 (src/proxy.conf.json)
npm run build
npm test         # Karma/Jasmine
```

## Backend architecture

Package root is `com.x7ubi.kurswahl`, split first **by role**: `admin/`, `student/`, `teacher/`, plus `common/`. Within each feature (e.g. `admin/classes`, `student/choice`) the layers are always `controller/`, `service/`, `mapper/`, `request/`, `response/`.

- **Entities and repositories are shared** in `common/models` and `common/repository`. The schema is managed by `spring.jpa.hibernate.ddl-auto=update`; there are no migrations.
- **Mappers** are MapStruct interfaces (`componentModel = "spring"`) that convert between entities and request/response DTOs.
- **Authorization uses custom annotations and AspectJ aspects**, not Spring Security method security. Each controller method is annotated with `@AdminRequired`, `@StudentRequired` or `@TeacherRequired` (in `<role>/authentication/`). The aspect reads the JWT from the request and checks that the user exists in the matching role repo. `@ChoiceOpenRequired` additionally blocks student choice endpoints when the `CHOICE_OPEN` setting (see `common/settings/SettingsService`) is false. Controllers get the current user via `jwtUtils.getUsernameFromAuthorizationHeader(authorization)`.
- **Errors**: services throw the custom exceptions in `common/exception` (`EntityNotFoundException` → 404, `EntityCreationException` → 400, `EntityDependencyException` → 409, `UnauthorizedException`, `DisabledException`, …), always with a constant from `ErrorMessage`. `GlobalExceptionHandler` turns these into a plain-string response body, and the frontend shows that string directly in a snackbar.
- Controllers carry springdoc/OpenAPI annotations (`@Operation`, `@ApiResponses`) that document every status code.
- Endpoints live under `/api/admin/...`, `/api/student/...`, `/api/teacher/...`, and the auth endpoints are in `common/auth`.

### Tests

Tests are Spring Boot integration tests of the **service layer**, not controllers. Annotate a test class with `@KurswahlServiceTest` (`src/test/java/com/x7ubi/kurswahl/KurswahlServiceTest.java`). It swaps in an H2 in-memory DB and uses `@DirtiesContext(BEFORE_EACH_TEST_METHOD)`, so each test gets a fresh context. Tests build their fixtures in `@BeforeEach` through the real repositories. Sonar coverage excludes controllers, mappers, DTOs, models, repos, config and aspects, so coverage expectations apply to services.

## Frontend architecture

- `src/app/component/` mirrors the backend roles: `admin/`, `student/`, `teacher/`, `auth/`, `common/`. Admin CRUD features follow a `show-*/create-*/edit-*` component pattern.
- Response/request TypeScript interfaces are kept per area in `admin.responses.ts`, `stundet.responses.ts` (sic), `teacher.response.ts` and `common.response.ts`.
- All HTTP goes through `service/http.service.ts`. Its callback-style `get/post/put/delete(url, body, onSuccess, onError?)` attaches the bearer token and handles errors globally: 401 logs out, 403 sends students back to `/student`, and every error message is shown in a snackbar. Use it instead of `HttpClient` directly.
- Route guards in `routing-helper/` (`LoginRequired`, `AdminRequired`, `StudentRequired`, `TeacherRequired`) are wired in `app.routes.ts`. Global providers and Material modules are set up in `app.config.ts`.

## CI/CD

`.github/workflows/pipeline.yml` runs on push and PR to `main` with these jobs: Maven build+test (JDK 21), then SonarCloud. On pushes to `main` it also deploys to production (https://kurswahl-pds.de) via `docker-compose up --build` on a self-hosted runner, then builds and deploys the Writerside docs to GitHub Pages. **Merging to `main` deploys to production.**
