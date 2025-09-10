# Repository Guidelines

## Project Structure & Modules
- `ps-be/`: Spring Boot backend (Java 8), configuration in `src/main/resources`, profiles via `application.yml` imports under `config/env/{dev|test|prod}`.
- `ps-fe/`: Vue 2 admin UI built with Rsbuild. Submodules live under `@fb/*`, app source in `src/`, public assets in `public/`.
- `docker-compose.yml`: Local stack (Keycloak + Postgres, MySQL, Redis, Nginx proxy).
- `logs/`, `uploads/`, `refer/`: runtime outputs and reference materials.

## Build, Test, and Development Commands
- Backend
  - `cd ps-be && mvn clean package`: build WAR with Spring Boot repackage.
  - `mvn spring-boot:run -Dspring-boot.run.profiles=dev`: run locally.
  - `mvn test`: run backend unit tests.
- Frontend
  - `cd ps-fe && npm ci`: install dependencies.
  - `npm run dev` (alias `serve`): start dev server.
  - `npm run build`: production build; preview via `npm run preview`.
  - `npm run eslint` and `npm run type-check`: lint and TS type check.
- Local services
  - From repo root: `docker compose up -d` to start Keycloak/Postgres, MySQL, Redis, and Nginx.

## Coding Style & Naming Conventions
- Java: 4-space indent, package names `lowercase.dot`, classes `PascalCase`, methods/fields `camelCase`. Prefer Lombok for boilerplate.
- Vue/JS: components `PascalCase` (files `kebab-case.vue`), modules `kebab-case.js/ts`, use ESLint and follow Vue 2 style guide. Less for styles.

## Testing Guidelines
- Backend: JUnit via `spring-boot-starter-test`. Place tests in `ps-be/src/test/java`. Add tests for services, mappers, and controllers; use `@SpringBootTest` only when necessary.
- Frontend: No unit test harness by default; run `npm run eslint` and `npm run type-check`. For UI changes, include manual testing steps in PR.

## Commit & Pull Request Guidelines
- Use Conventional Commits: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`. Example: `feat(admin): add role assignment API`.
- PRs must include: clear description, linked issue (if any), screenshots/GIFs for UI, steps to reproduce/verify, and notes on config changes.

## Security & Configuration Tips
- Do not commit secrets. Env files: `ps-fe/.env.dev|.env.prod|.env.test`. Backend profiles in `ps-be/src/main/resources/config/env/*`.
- Default profile is `dev`; never change defaults for `prod` locally. Access Keycloak at `http://localhost:18080` (admin `admin/admin123`).
