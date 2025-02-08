package com.portfolio.probeanalyzer.producer.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProbeResult {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("uptime")
    private String uptime;

    @JsonProperty("uptimeSeconds")
    private long uptimeSeconds;

    @JsonProperty("tasksTotal")
    private int tasksTotal;

    @JsonProperty("workersTotal")
    private int workersTotal;

    @JsonProperty("executorsTotal")
    private int executorsTotal;

    @JsonProperty("replicationCount")
    private int replicationCount;

    @JsonProperty("requestedMemOnHeap")
    private int requestedMemOnHeap;

    @JsonProperty("requestedMemOffHeap")
    private int requestedMemOffHeap;

    @JsonProperty("requestedTotalMem")
    private int requestedTotalMem;

    @JsonProperty("requestedCpu")
    private int requestedCpu;

    @JsonProperty("assignedMemOnHeap")
    private int assignedMemOnHeap;

    @JsonProperty("assignedMemOffHeap")
    private int assignedMemOffHeap;

    @JsonProperty("assignedTotalMem")
    private int assignedTotalMem;

    @JsonProperty("assignedCpu")
    private int assignedCpu;

    @JsonProperty("schedulerDisplayResource")
    private boolean schedulerDisplayResource;
}