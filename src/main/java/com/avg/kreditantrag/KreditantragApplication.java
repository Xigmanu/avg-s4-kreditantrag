package com.avg.kreditantrag;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.avg.kreditantrag.internal.caching.OkHttpClientConfig.CACHE_DIR;

/**
 * Main class for spring application.
 */
@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = "classpath*:/model/*.bpmn")
public class KreditantragApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    public static void main(String... args) {
        SpringApplication.run(KreditantragApplication.class, args);

        LOGGER.info("Clearing HTTP cache");
        if (Files.exists(Path.of(CACHE_DIR))) {
            clearDir(new File(CACHE_DIR));
        }
    }

    private static void clearDir(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        Arrays.stream(files)
                .toList()
                .forEach(file -> {
                    if (file.delete()) {
                        return;
                    }
                    throw new SecurityException();
                });

        LOGGER.debug("Cleared {} files from cache", files.length);
    }
}
