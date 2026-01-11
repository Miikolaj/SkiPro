# SkiProV2 backend – PostgreSQL (Docker)

## Run database

From `backend/`:

```powershell
docker compose up -d
```

This starts PostgreSQL on `localhost:5432` with:
- database: `skipro`
- user: `skipro`
- password: `skipro`

## Run app

```powershell
.\gradlew.bat bootRun
```

The app uses `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skipro
spring.datasource.username=skipro
spring.datasource.password=skipro
```

## Stop database

```powershell
docker compose down
```

If you want to remove persisted DB data:

```powershell
docker compose down -v
```

