package com.avg.kreditantrag.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ZeebeClientBuilder zeebeClient() {
        return ZeebeClient.newClientBuilder()
                .gatewayAddress("127.0.0.1:26500")
                .usePlaintext();
    }
}
