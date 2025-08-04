# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot application built with Kotlin that integrates with OpenAI API via Spring AI. Features a chat service with PII scrubbing capabilities, audit logging, and caching.

## Key Architecture

- **ChatService**: Core service handling AI interactions with caching and PII protection
- **AuditService**: Logs chat interactions to PostgreSQL database  
- **PII Scrubbers**: Uses pythonbyte-krux library for scrubbing emails, phone numbers, and government IDs
- **DatabaseInitializer**: Automatically creates PostgreSQL database on startup

## Essential Commands

### Build and Run
```bash
./mvnw spring-boot:run                 # Run the application
./mvnw clean compile                   # Compile the project
./mvnw clean package                   # Build JAR file
```

### Testing
```bash
./mvnw test                           # Run all tests
./mvnw test -Dtest=ChatServiceTest    # Run specific test class
```

### Code Quality
```bash
./mvnw ktlint:check                   # Check Kotlin code style
./mvnw ktlint:format                  # Auto-format Kotlin code
```

## Configuration

Environment variables required:
- `OPEN_AI_API_KEY`: OpenAI API key for chat functionality
- `POSTGRES_JDBC_URL`: PostgreSQL connection URL
- `POSTGRES_USERNAME`: Database username  
- `POSTGRES_PASSWORD`: Database password

Key properties in `application.properties`:
- `chat.service.scrub.pii=true`: Enable/disable PII scrubbing
- `chat.service.cache.size=100`: Response cache size
- `chat.service.auditing.enabled=true`: Enable chat audit logging

## Important Constraints

- **Kotlin Only**: Per `.cursor/rules/kotlin-only.mdc`, all new code must be written in Kotlin (.kt files), not Java
- **System Dependency**: Uses local JAR `lib/pythonbyte-krux-jar-with-dependencies.jar` for PII scrubbing
- **Database**: PostgreSQL required for production, H2 used for tests

## Project Structure

```
src/main/kotlin/com/pythonbyte/spring_into_ai/
├── SpringIntoAiApplication.kt        # Main application class
├── config/DatabaseInitializer.kt     # Database setup
├── entities/ChatAuditLog.kt          # JPA entity for audit logs
├── repositories/                     # Spring Data JPA repositories
├── services/                         # Business logic (ChatService, AuditService)
└── utils/scrubbers/                  # PII scrubbing utilities
```

## Dependencies

- Spring Boot 3.5.3 with Spring AI 1.0.0
- Kotlin 2.0.21 with Java 24 target
- PostgreSQL for production database
- H2 for test database
- Custom pythonbyte-krux library for PII protection