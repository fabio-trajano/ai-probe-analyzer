package com.portfolio.probeanalyzer.producer.probe;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProbeGenerator {
    private static final List<String> STATUSES = List.of(
            "ACTIVE", "KILLED", "ACTIVE", "BALANCING", "ACTIVE", "INACTIVE", "ACTIVE"
    );

    private static final List<String> TOPOLOGY_NAMES = List.of(
            "STORM-TLA", "TOPOLOGY", "SERVICE"
    );

    public static ProbeResult generateProbe() {
        String name = randomChoice(TOPOLOGY_NAMES);
        String id = generateRandomId(name);

        return ProbeResult.builder()
                .id(id)
                .name(name)
                .status(randomChoice(STATUSES))
                .uptime(generateUptime())
                .uptimeSeconds(ThreadLocalRandom.current().nextLong(0, 3600)) // 0-1 hour
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
                .schedulerDisplayResource(true)
                .build();
    }

    private static String generateRandomId(String name) {
        return name + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private static String generateUptime() {
        long minutes = ThreadLocalRandom.current().nextLong(0, 60);
        long seconds = ThreadLocalRandom.current().nextLong(0, 60);
        return String.format("%dm %ds", minutes, seconds);
    }

    private static <T> T randomChoice(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    private static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
