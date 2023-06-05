package com.avg.kreditantrag;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for spring application.
 */
@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = "classpath*:/model/*.bpmn")
public class KreditantragApplication {
    public static void main(String... args) {
        SpringApplication.run(KreditantragApplication.class, args);
    }
}
