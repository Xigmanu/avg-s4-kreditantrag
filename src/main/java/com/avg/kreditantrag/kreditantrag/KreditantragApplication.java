package com.avg.kreditantrag.kreditantrag;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
@ZeebeDeployment(resources = "classpath*:/model/*.bpmn")
public class KreditantragApplication {

    public static void main(String... args) {
        SpringApplication.run(KreditantragApplication.class, args);
    }
}
