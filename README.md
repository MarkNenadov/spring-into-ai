# spring-into-ai (Chat Service)

A Spring Boot application demonstrating the integration of AI capabilities through Spring AI, featuring a chat service with privacy and performance optimizations.

## Features

- **AI-Powered Chat Service**: Integration with OpenAI's API for intelligent conversation handling
- **PII Protection**: Built-in Personally Identifiable Information (PII) scrubbing

## Prerequisites

- PostgreSQL database
- OpenAI API key

## Configuration

### Database Setup

Configure your PostgreSQL connection using the following environment variables:

```bash
POSTGRES_JDBC_URL=your_jdbc_url
POSTGRES_USERNAME=your_username
POSTGRES_PASSWORD=your_password
```

The database schema will be automatically created on application startup.

### OpenAI Integration

Set your OpenAI API key using the environment variable:

```bash
OPEN_AI_API_KEY=your_api_key
```

> **Note**: It's recommended to use environment variables rather than hardcoding sensitive information in `application.properties`.

## Customization

### PII Scrubbing

The chat service includes built-in PII protection that redacts:
- Email addresses
- Social Insurance Numbers (SINs)
- Social Security Numbers (SSNs)
- Phone numbers

To configure PII scrubbing, modify the following property in `application.properties`:

```properties
chat.service.scrub.pii=true
```

### Cache Configuration

The service implements a response cache to improve performance. Configure the cache size in `application.properties`:

```properties
chat.service.cache.size=100
```

## Getting Started

1. Clone the repository
2. Configure your environment variables
3. Run the application using `./mvnw spring-boot:run`

## License

This project is licensed under the MIT License - see the LICENSE file for details.
