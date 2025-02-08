# **AI Probe Analyzer**
- AI Probe Analyzer is a Spring Boot application that generates and consumes “probe” events (e.g., from Storm topologies or external health checks) via Kafka, then uses an LLM (via Ollama) to classify each probe’s status as OK, WARNING, or ERROR based on configurable logic.
