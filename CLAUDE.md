# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a complete enterprise-grade Single Sign-On (SSO) system built with Keycloak integration, consisting of a Spring Boot backend and Vue.js frontend. The project demonstrates SSO authentication across multiple systems with token validation, user management, and administrative features.

## Architecture

### Multi-Module Structure
- **ps-be/**: Spring Boot backend application (Java 8, Maven)
- **ps-fe/**: Vue.js 2.6 frontend application (Node.js, Rsbuild)
- **refer/**: Documentation and configuration guides
- **docker-compose.yml**: Full containerized deployment setup

### Backend Architecture (ps-be/)
- **Framework**: Spring Boot 2.7.18 with MyBatis Plus
- **Security**: Spring Security + Keycloak integration
- **Database**: MySQL 5.7 with Druid connection pool
- **Caching**: Redis integration
- **Main Package Structure**:
  - `com.jiuxi.admin`: Core admin functionality and entity management
  - `com.jiuxi.module.*`: Domain modules (user, org, auth, sys)
  - Multi-environment configuration (dev/test/prod)

### Frontend Architecture (ps-fe/)
- **Framework**: Vue.js 2.6.12 with JPX3.0 platform
- **Build Tool**: Rsbuild (modern alternative to webpack)
- **UI Components**: Custom component library (@fb packages)
- **State Management**: Vuex 3.x
- **Routing**: Vue Router 3.x
- **Modular Structure**: 
  - `@fb/`: Framework component packages (admin-base, fb-core, fb-ui, etc.)
  - `src/`: Main application
  - `screen/`: Data visualization dashboard

## Common Development Commands

### Backend (ps-be/)
```bash
# Build and run backend
cd ps-be
mvn clean compile
mvn spring-boot:run

# Run tests
mvn test

# Package WAR file
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

### Frontend (ps-fe/)
```bash
# Install dependencies
npm install

# Development server (port 10801)
npm run dev
# or
npm run serve

# Build for production
npm run build

# Code linting
npm run eslint

# Type checking
npm run type-check
```

### Docker Deployment
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and restart
docker-compose up -d --build
```

## Environment Configuration

### Backend Configuration
- **Dev Environment**: Uses `config/env/dev/` configuration files
- **Database**: Configuration in `config/db/database-{env}.yml`
- **Security**: Keycloak settings in `config/sec/security-{env}.yml`
- **Caching**: Redis config in `config/cache/cache-{env}.yml`

### Frontend Configuration
- **Dev Server**: http://localhost:10801
- **Environment Files**: `.env.dev`, `.env.prod`, `.env.test`
- **Project Config**: `project.config.js` for theme, services, and routing
- **Build Config**: `rsbuild.config.js` for build optimization

## Key Integration Points

### Keycloak SSO Integration
- **Backend**: Uses Keycloak Admin Client (v22.0.5) for user management
- **Frontend**: Uses `keycloak-js` (v26.2.0) for client-side authentication
- **Token Flow**: JWT tokens for stateless authentication
- **Multi-tenant**: Supports multiple realms and clients

### API Architecture
- **Base URL**: `/ps-be` (configured in project.config.js)
- **Authentication**: Bearer token in Authorization header
- **Response Format**: Standardized JSON responses with status codes
- **Error Handling**: Global exception handling with i18n support

### Database Schema
- **Prefix Convention**: All tables use `tp_` prefix
- **Key Entities**:
  - `tp_account*`: User account management
  - `tp_person*`: Personal information
  - `tp_role*`, `tp_menu*`: RBAC system
  - `tp_dept*`, `tp_org*`: Organization structure
  - `tp_system_config`: System configuration

## Development Guidelines

### Code Organization
- **Backend**: Follow domain-driven design with clear module separation
- **Frontend**: Use JPX3.0 platform conventions with modular component architecture
- **Naming**: Use consistent naming patterns (CamelCase for Java, kebab-case for Vue components)

### Security Practices
- Password encryption with SM-crypto
- XSS protection with JSoup sanitization
- CSRF protection enabled
- Input validation on both frontend and backend

### Testing Strategy
- Backend: Unit tests with Spring Boot Test
- Frontend: Component testing (setup required)
- Integration: Docker-based environment testing

## Troubleshooting

### Common Build Issues
- **Backend**: Ensure Java 8 and Maven 3.6+ are installed
- **Frontend**: Ensure Node.js 14+ and npm 6+ are installed
- **Docker**: Check port conflicts (3306, 5432, 6379, 8080, 18080)

### Development Server Issues
- **Backend Port**: Default 8088 (configurable in application.yml)
- **Frontend Port**: Default 10801 (configurable in project.config.js)
- **Database**: MySQL on 3306, PostgreSQL on 5432, Redis on 6379

### Authentication Issues
- **Keycloak URL**: Default http://localhost:18080
- **Admin Credentials**: admin/admin123
- **Client Configuration**: Check redirect URIs match application URLs

## Service Dependencies

### Required Services
- **MySQL 5.7**: Main application database
- **PostgreSQL 15**: Keycloak database  
- **Redis 7**: Caching and session storage
- **Keycloak 23.0.1**: Identity and access management
- **Nginx**: Reverse proxy and static file serving

### Service Startup Order
1. Databases (MySQL, PostgreSQL)
2. Redis cache
3. Keycloak server
4. Backend application
5. Nginx proxy
6. Frontend development server

## Important File Locations

### Configuration Files
- `ps-be/src/main/resources/application.yml`: Main backend config
- `ps-fe/project.config.js`: Frontend project configuration
- `ps-fe/rsbuild.config.js`: Build configuration
- `docker-compose.yml`: Container orchestration

### Key Source Files
- `ps-be/src/main/java/com/jiuxi/Application.java`: Backend entry point
- `ps-fe/src/main.js`: Frontend entry point
- `ps-fe/@fb/fb-core/`: Core framework functionality
- `ps-be/src/main/resources/mapper/`: MyBatis XML mappings

This project requires careful coordination between Keycloak configuration, backend security setup, and frontend token handling to achieve seamless SSO functionality.