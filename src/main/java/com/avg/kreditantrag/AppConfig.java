package com.avg.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * A configuration class with bean methods.
 */
@Configuration
@SuppressWarnings("SpellCheckingInspection")
public class AppConfig {
    
    @Value("${zeebe.client.cloud.region}")
    private String region;

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
    @Scope("singleton")
    public ZeebeClientBuilder zeebeClientBuilderCloud() {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId(clusterId)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withRegion(region);
    }

    /**
     * A bean method that creates a new instance of {@code ZeebeClientBuilder} class
     * configured for execution in self-managed environment.
     *
     * @return a new instance of {@code ZeebeClientBuilder}
     */
    @Bean
    @Profile("self-managed")
    @Scope("singleton")
    public ZeebeClientBuilder zeebeClientBuilderLocal() {
        return ZeebeClient.newClientBuilder()
                .gatewayAddress(gatewayAddress)
                .usePlaintext();
    }
}
