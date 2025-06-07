# spring-into-ai

---

My experiments with Spring AI in the form of a Spring Boot Application that has a ChatService.

---
### Setting up Postgres

Set environment variables for POSTGRES_JDBC_URL, POSTGRES_USERNAME, and POSTGRES_PASSWORD.

Database will be created automatically.


### Setting your Open AI API Key

Use the OPEN_AI_API_KEY environment variable.

(Hardcoding it into application.properties not recommended)

---

### PII Scrubbing

ChatService has basic PII scrubbing. It redacts email addresses, SINs/SSNs, and phone numbers.

PII scrubbing defaults to on. To turn scrubbing off/on, there is a flag:

```
chat.service.scrub.pii = true
```

### ChatService Caching

There is a basic cache of prompts/answers in ChatService.

It defaults to hold a maximum of 100 items. This can be changed in application.properties:

```
chat.service.cache.size = 100
```
