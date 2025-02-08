package com.portfolio.probeanalyzer.producer.probe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for all probe results.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,         // store subtype info as a "name"
        include = JsonTypeInfo.As.PROPERTY, // put it in a JSON property
        property = "type"                   // the JSON field: "type": "STORM"/"HEALTHCHECK"/etc.
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StormProbeResult.class, name = "STORM"),
        @JsonSubTypes.Type(value = HealthCheckProbeResult.class, name = "HEALTHCHECK")
})
public abstract class ProbeResult {

    private long timestamp;

    public ProbeResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
