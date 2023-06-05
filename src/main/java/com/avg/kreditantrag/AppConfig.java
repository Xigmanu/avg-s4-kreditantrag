package com.avg.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * A configuration class with bean methods.
 */
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

    /**
     * A bean method that creates a new instance of {@code ZeebeClientBuilder} class
     * configured for execution in Camunda cloud (SaaS)
     *
     * @return a new instance of {@code ZeebeClientBuilder}
     */
    @Bean
    @Profile("cloud")
    public ZeebeClientBuilder zeebeClientBuilderCloud() {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId(clusterId)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRegion("dsm-1");
    }

    /**
     * A bean method that creates a new instance of {@code ZeebeClientBuilder} class
     * configured for execution in self-managed environment.
     *
     * @return a new instance of {@code ZeebeClientBuilder}
     */
    @Bean
    @Profile("self-managed")
    public ZeebeClientBuilder zeebeClientBuilderLocal() {
        return ZeebeClient.newClientBuilder()
                .gatewayAddress(gatewayAddress)
                .usePlaintext();
    }
}
