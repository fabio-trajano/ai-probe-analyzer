package com.portfolio.probeanalyzer.consumer.kafka;

import com.portfolio.probeanalyzer.consumer.analyzer.ProbeAnalyzerService;
import com.portfolio.probeanalyzer.producer.probe.HealthCheckProbeResult;
import com.portfolio.probeanalyzer.producer.probe.ProbeResult;
import com.portfolio.probeanalyzer.producer.probe.StormProbeResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ProbeAnalyzerService probeAnalyzerService;

    public KafkaConsumerService(ProbeAnalyzerService probeAnalyzerService) {
        this.probeAnalyzerService = probeAnalyzerService;
    }

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "ai-probe-analyzer-group")
    public void consume(ConsumerRecord<String, ProbeResult> record) {
        ProbeResult probe = record.value();
        LOGGER.info("Received probe: {}", probe);

        // If you need special logic:
        if (probe instanceof StormProbeResult storm) {
            // handle Storm
            // pass to analyzer, or do something
        } else if (probe instanceof HealthCheckProbeResult hc) {
            // handle HealthCheck
        }

        try {
            String analysisResult = probeAnalyzerService.analyze(probe);
            LOGGER.info("Analysis Result: {}", analysisResult);
        } catch (Exception e) {
            LOGGER.error("Error processing probe: {}", e.getMessage(), e);
        }
    }
}
