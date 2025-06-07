# spring-into-ai

---

My experiments with Spring AI in the form of a Spring Boot Application that has a ChatService.

---
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

For now it only scrubs phone numbers.