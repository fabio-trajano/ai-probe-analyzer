package com.portfolio.probeanalyzer.producer.probe;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProbeGenerator {

    private static final List<String> PROBE_TYPES = List.of("STORM", "HEALTHCHECK");

    public static ProbeResult generateProbe() {
        // 50/50 chance - pick which type to generate
        String pick = randomChoice(PROBE_TYPES);
        if ("STORM".equals(pick)) {
            return generateStormProbe();
        } else {
            return generateHealthCheckProbe();
        }
    }

    private static StormProbeResult generateStormProbe() {

        String name = randomChoice(List.of("STORM-TLA", "TOPOLOGY", "SERVICE"));
        String id = name + "-" + randomInt(1000, 9999);

        return StormProbeResult.builder()
                .id(id)
                .name(name)
                .status(randomChoice(List.of("ACTIVE", "KILLED", "BALANCING", "INACTIVE")))
                .uptime(generateUptime())
                .uptimeSeconds(ThreadLocalRandom.current().nextLong(0, 3600))
                .tasksTotal(randomInt(1, 50))
                .workersTotal(randomInt(1, 10))
                .executorsTotal(randomInt(1, 50))
                .replicationCount(randomInt(1, 3))
                .requestedMemOnHeap(randomInt(128, 1024))
                .requestedMemOffHeap(randomInt(64, 512))
                .requestedTotalMem(randomInt(192, 1536))
                .requestedCpu(randomInt(10, 100))
                .assignedMemOnHeap(randomInt(128, 1024))
                .assignedMemOffHeap(randomInt(64, 512))
                .assignedTotalMem(randomInt(192, 1536))
                .assignedCpu(randomInt(10, 100))
                .build();
    }

    private static HealthCheckProbeResult generateHealthCheckProbe() {
        String name = randomChoice(List.of("ECR-Service-", "MANAGER-SERVICE", "SERVICE"));
        return HealthCheckProbeResult.builder()
                .serviceName(name + randomInt(1000, 9999))
                .healthy(ThreadLocalRandom.current().nextBoolean())
                .uptime(generateUptime())
                .build();
    }


    private static <T> T randomChoice(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static String generateUptime() {
        long minutes = ThreadLocalRandom.current().nextLong(0, 60);
        long seconds = ThreadLocalRandom.current().nextLong(0, 60);
        return String.format("%dm %ds", minutes, seconds);
    }
}
