# **AI Probe Analyzer**
- AI Probe Analyzer is a Spring Boot application that generates and consumes simulated “probe” events (e.g., from Storm topologies or external health checks) via **Kafka**, then uses a **LLM** (running locally via Ollama) to classify each probe’s status as `OK`, `WARNING`, or `ERROR` based on configurable logic.

---

## Overview

The **AI Probe Analyzer** acts as both **Kafka producer** and **consumer**:

1. **Producer**
    - Periodically generates random probes (e.g., [StormProbeResult](./src/main/java/com/portfolio/probeanalyzer/producer/probe/StormProbeResult.java) or [HealthCheckProbeResult](./src/main/java/com/portfolio/probeanalyzer/producer/probe/HealthCheckProbeResult.java)).
    - Serializes them as JSON and publishes to a Kafka topic (`probe-result-topic`).

2. **Consumer**
    - Listens on the same topic.
    - Uses an LLM (served locally by Ollama) to analyze each probe.
    - Classifies each probe as **OK**, **WARNING**, or **ERROR** according to user-defined or AI-driven logic.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Kafka** for producing/consuming
- **Docker** Compose integration
- **Apache Kafka** + **Zookeeper** (via Confluent images)
- **Lombok** for boilerplate reduction
- **Jackson** for JSON
- **Ollama** (serving Llama2 or similar)


---

## Getting Started

### Prerequisites

- **Java 21** (or higher)
- **Maven 3.8+**
- **Docker** & **Docker Compose**
- **Ollama** installed (and a compatible model, e.g., `llama2`)

### Installation & Run

1. **Clone** this repository:
   ```bash
   git clone https://github.com/your-username/ai-probe-analyzer.git
   cd ai-probe-analyzer
   ```
2. **Setup** Ollama:
   - Install Ollama and pull the desired model(e.g., `llama2`).
   - On application.properties, set the `ollama.model` property to the pulled model.
- Start Ollama:
   
    ```bash
    ollama serve
    # By default, Ollama listens on localhost:11434
    ```
3. **Start** Kafka and Zookeeper via Docker Compose::
    ```bash
    docker-compose up -d
    ```

4. **Run** the Spring Boot app:

    ```bash
    mvn clean install -DskipTests && mvn spring-boot:run
    ```
This will:

- Start scheduling probe generation.
- Produce probes to Kafka.
- Consume them from Kafka.
- Call Ollama to classify each probe.


  - Check Logs:

You should see logs indicating “Probe sent successfully” from the producer.
Then logs from the consumer showing “Received probe” and “Analysis Result: …”.
