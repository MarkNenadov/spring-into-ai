# spring-into-ai

---

My experiments with Spring AI

---
### Setting your Open AI API Key

Use the OPEN_AI_API_KEY environment variable.

(Hardcoding it into application.properties not recommended)

---

### Configuring PII Scrubbing

Go to application.properties and there is a flag:

```
chat.service.scrub.pii = true
```

For now it only scrubs phone numbers.