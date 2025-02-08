package com.portfolio.probeanalyzer.producer.probe;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("HEALTHCHECK")
public class HealthCheckProbeResult extends ProbeResult {
    private String serviceName;
    private boolean healthy;
    private String uptime;
}
