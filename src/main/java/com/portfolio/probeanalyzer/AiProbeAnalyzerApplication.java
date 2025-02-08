package com.portfolio.probeanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiProbeAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiProbeAnalyzerApplication.class, args);
	}

}
