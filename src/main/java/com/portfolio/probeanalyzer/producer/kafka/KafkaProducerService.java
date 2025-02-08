package com.portfolio.probeanalyzer.producer.kafka;

import com.portfolio.probeanalyzer.producer.probe.ProbeGenerator;
import com.portfolio.probeanalyzer.producer.probe.ProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class KafkaProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${kafka.topic.name}")
    private String topic;

    @Value("${probe.generation.rate}")
    private int rate;

    @Autowired
    private KafkaTemplate<String, ProbeResult> kafkaTemplate;

    @Scheduled(fixedRateString = "${probe.generation.rate}")
    public void sendProbeResult() {
        ProbeResult probe = ProbeGenerator.generateProbe();

        try {
            kafkaTemplate.send(topic, probe.getId(), probe)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            LOGGER.info("Probe sent successfully: id={}, name={}, status={}",
                                    probe.getId(), probe.getName(), probe.getStatus());
                        } else {
                            LOGGER.error("Failed to send probe: id={}", probe.getId(), ex);
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("Error while sending probe: id={}", probe.getId(), e);
        }
    }
}