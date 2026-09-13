# React + Spring Boot Template (Mono Repo)

Docker template for **React SPA frontend** + **Java Spring Boot backend** + **Postgres**, with all config in one place.

> This template ships **only 4 files — no app code**. You add your own code:
>
> ```text
> /
>   README.md              # you are here — tutorial + how to run
>   docker-compose.yaml    # ALL env lives here (no .env file), Postgres by default
>   backend/Dockerfile     # multi-stage builder for your Spring Boot app
>   frontend/Dockerfile    # multi-stage builder for your React SPA
> ```

| Part | Expected stack | Dockerfile stages | Port |
|------|---------------|-------------------|------|
| `frontend/` | React SPA (Vite + npm) — you add `package.json`, `src/`, `index.html` | `node:20-alpine` build → `nginx:alpine` serve | `3000:80` |
| `backend/` | Java Spring Boot (Maven) — you add `pom.xml`, `src/` | `maven:3.9-eclipse-temurin-21` build → `eclipse-temurin:21-jre` run | `8080:8080` |
| `db` | Postgres 16 (`postgres:16-alpine` + `pgdata` volume) | — | `5432:5432` |

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (includes `docker compose`)
- Git

Check:

```bash
docker --version
docker compose version
```

---

## 1. Use this template

**Option A — GitHub template (recommended):**
1. Click **Use this template → Create a new repository**.
2. Clone it:
   ```bash
   git clone https://github.com/<you>/<your-repo>.git
   cd <your-repo>
   ```

**Option B — Clone directly:**
```bash
git clone https://github.com/<you>/Template.git my-app
cd my-app
```

**Add your code:**
- `backend/`: drop in a standard Maven Spring Boot project (`pom.xml` + `src/`). It must build to `target/*.jar` and listen on `${SERVER_PORT:8080}`.
- `frontend/`: drop in a standard Vite React project (`package.json` + `src/` + `index.html`). It must build with `npm run build` to `dist/`. Read the backend URL from `VITE_API_URL`.

---

## 2. How to run the project

> **Note — which way should I run it?**
> - **Full stack at once?** Use **Docker Compose** (Way A). No Node/Java setup needed. This is the default way for this template.
> - **Coding with hot-reload?** Run only `db` in Docker (`docker compose up db -d`), then run frontend/backend locally with `npm run dev` / `./mvnw spring-boot:run` and point them at `localhost:5432`.

### Way A — Full stack with Docker Compose (recommended)

From the repo **root** (where `docker-compose.yaml` lives):

```bash
# Build images and start all 3 services
docker compose up --build
```

Then open:

| What | URL |
|------|-----|
| Frontend | http://localhost:3000 |
| Backend | http://localhost:8080 (your own endpoints) |
| Postgres | `localhost:5432`, db `appdb`, user `postgres`, pass `postgres` |

Run in background / stop:

```bash
docker compose up --build -d   # detached
docker compose logs -f         # follow all logs
docker compose logs -f backend # one service only
docker compose ps
docker compose down            # stop (keep DB data in pgdata volume)
docker compose down -v         # stop AND delete DB data (fresh start)
```

Rebuild after changing code or env:

```bash
docker compose up --build
```

### Way B — Only the database in Docker (local dev)

```bash
docker compose up db -d
# now connect your locally-run apps to localhost:5432 (appdb/postgres/postgres)
```

### Quick smoke test

```bash
docker compose config          # validate compose file
curl http://localhost:3000     # frontend (nginx)
# plus whatever health endpoint YOUR backend exposes, e.g.:
# curl http://localhost:8080/actuator/health
```

---

## 3. Configuration — all env is in `docker-compose.yaml`

There is **no `.env` file**. Every setting is hardcoded under `environment:` so it is visible in one place. Edit the file and re-run `docker compose up --build`.

### Database (`db` service)

```yaml
POSTGRES_DB: appdb
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres   # change me for real projects!
```

Data persists in the `pgdata` volume. Connect from outside Docker at `localhost:5432`.

### Backend (`backend` service)

| Var | Default in compose | Your app should do |
|-----|--------------------|--------------------|
| `SERVER_PORT` | `8080` | `server.port=${SERVER_PORT:8080}` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db:5432/appdb` | `spring.datasource.url=${SPRING_DATASOURCE_URL:...}` (`db` = compose hostname) |
| `SPRING_DATASOURCE_USERNAME` / `_PASSWORD` | `postgres` / `postgres` | Must match `db` service |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Use `validate`/`none` in prod |
| `APP_CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Allow the frontend origins |
| `MANAGEMENT_*` | `health,info` | Actuator exposure (used by the `healthcheck:`) |

### Frontend (`frontend` service)

| Build arg | Default | Meaning |
|-----------|---------|---------|
| `VITE_API_URL` | `http://localhost:8080` | Baked into the SPA at `docker build` time. Browsers call this directly, so it must be reachable from your machine. Read it in code via `import.meta.env.VITE_API_URL`. |

> Changing `VITE_API_URL` requires a rebuild (`docker compose up --build`) because Vite inlines it into the static JS.

---

## 4. Dockerfiles (multi-stage, self-contained)

**`backend/Dockerfile`:**
1. `maven:3.9-eclipse-temurin-21` — copies YOUR `pom.xml` + `src/`, runs `mvn package` → `app.jar`
2. `eclipse-temurin:21-jre-jammy` + `curl` — runs `java -jar app.jar`, exposes `8080`

**`frontend/Dockerfile`:**
1. `node:20-alpine` — copies YOUR `package.json` + source, `npm ci || npm install`, `npm run build` → `dist/` (with `ARG VITE_API_URL`)
2. `nginx:alpine` — creates its own SPA nginx config inline (`try_files ... /index.html`), serves `dist/`, exposes `80`

Neither Dockerfile needs any other template file.

---

## 5. Troubleshooting

| Symptom | Fix |
|---------|-----|
| `port is already allocated` (3000/8080/5432) | Stop the other app, or change `ports:` in `docker-compose.yaml` |
| Backend exits, `Connection to db:5432 refused` | Wait for `db` healthcheck; `docker compose logs db`. Inside Compose the DB host must be `db`, not `localhost`. |
| Frontend can't reach backend | Check `VITE_API_URL` build arg, then rebuild with `--build`. |
| Stale DB state | `docker compose down -v` (deletes `pgdata`), then `up --build`. |
| First build is slow | Normal — Maven/npm dependencies download once, then layers are cached. |
| `docker compose config` fails | Validate YAML indent; run `docker compose config` to see the resolved file. |

---

## 6. Useful commands

```bash
docker compose config              # validate + print resolved compose file
docker compose up --build          # (re)build + run everything
docker compose up db -d            # only Postgres (for local dev)
docker compose exec db psql -U postgres -d appdb   # psql shell
```

## Next steps

- [ ] Add your `backend/` (Spring Initializr: Web, Validation, JPA, PostgreSQL, Actuator) and `frontend/` (Vite React) code
- [ ] Change `POSTGRES_PASSWORD` and use secrets for production
- [ ] Set `SPRING_JPA_HIBERNATE_DDL_AUTO: validate` + Flyway/Liquibase migrations
- [ ] Add CI (build + test + `docker compose config`)
