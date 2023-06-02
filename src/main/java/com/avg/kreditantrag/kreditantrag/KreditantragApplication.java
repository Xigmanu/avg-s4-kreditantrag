package com.avg.kreditantrag.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
@ZeebeDeployment(resources = "classpath:testProcess.bpmn")
public class KreditantragApplication {

    public static void main(String... args) {
        SpringApplication.run(KreditantragApplication.class, args);
    }
}
