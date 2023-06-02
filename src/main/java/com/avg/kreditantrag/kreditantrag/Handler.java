package com.avg.kreditantrag.kreditantrag;

import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.stereotype.Component;

@Component
public class Handler {
    @ZeebeWorker(type = "test1", autoComplete = true)
    public void handle() {
        System.out.println("Hallo aus test1!!!");
    }
}
