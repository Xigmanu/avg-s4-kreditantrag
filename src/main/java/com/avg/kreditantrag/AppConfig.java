package com.avg.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Value("${zeebe.client.cloud.clusterId}")
    private String clusterId;

    @Value("${zeebe.client.cloud.clientId}")
    private String clientId;

    @Value("${zeebe.client.cloud.clientSecret}")
    private String clientSecret;

    @Value("${zeebe.client.broker.gateway-address}")
    private String gatewayAddress;

    @Bean
    @Profile("cloud")
    public ZeebeClientBuilder zeebeClientBuilderCloud() {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId(clusterId)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRegion("dsm-1");
    }
    @Bean
    @Profile("self-managed")
    public ZeebeClientBuilder zeebeClientBuilderLocal() {
        return ZeebeClient.newClientBuilder()
                .gatewayAddress(gatewayAddress)
                .usePlaintext();
    }
}
