package com.portfolio.probeanalyzer.consumer.analyzer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.probeanalyzer.producer.probe.ProbeResult;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProbeAnalyzerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbeAnalyzerService.class);

    @Value("${ollama.api.url:http://localhost:11434/api/generate}")
    private String ollamaApiUrl;

    @Value("${ollama.model:llama2}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyze(ProbeResult probe) {
        String prompt = buildPrompt(probe);

        try {
            return callOllama(prompt);
        } catch (Exception e) {
            LOGGER.error("Error analyzing probe: {}", e.getMessage(), e);
            return "AI SERVER ERROR";
        }
    }

    private String buildPrompt(ProbeResult probe) {
        return String.format(
                """
                You are a strict classification model.
                You will receive probes from Storm Topologies, Health Checks, or other monitoring systems.
                Your task is to classify the probe data into one of the following categories:
                  - OK
                  - WARNING
                  - ERROR
        
                Classification rules in plain English:
                - If type=STORM and status=ACTIVE => classification=OK
                - If type=STORM and status=INACTIVE => classification=ERROR
                - If type=STORM and status=BALANCING or KILLED => classification=WARNING
                - If type=HEALTH_CHECK and status=UP or health=true => classification=OK
                - if type=HEALTH_CHECK and status=DOWN or health=false => classification=ERROR
        
                When classification is ERROR, provide a short reason in the "reason" field
                (e.g. "status=INACTIVE", "unhealthy check", etc.). If classification is OK or WARNING,
                do not include a reason or set it to an empty string.
        
                The probe data is:
                %s
        
                Output EXACTLY in this format (no extra text or lines):
        
                probeId: <the probe id>
                probeType: <the probe type>
                classification: <OK | WARNING | ERROR>
                reason: <short reason only if classification=ERROR>
                """,
                probe.toString()
        );
    }


    private String callOllama(String prompt) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ollamaApiUrl);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", model);
        requestBodyMap.put("prompt", prompt);
        requestBodyMap.put("stream", false);

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
        httpPost.setEntity(new StringEntity(requestBody));
        httpPost.setHeader("Content-Type", "application/json");

        return httpClient.execute(httpPost, response -> {
            try {
                return processOllamaResponse(response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String processOllamaResponse(ClassicHttpResponse response) throws Exception {
        int statusCode = response.getCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        if (statusCode >= 200 && statusCode < 300) {
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            return jsonResponse.get("response").asText().trim();
        } else {
            LOGGER.error("Ollama API returned error: HTTP {} - {}", statusCode, responseBody);
            return "ERROR";
        }
    }
}
