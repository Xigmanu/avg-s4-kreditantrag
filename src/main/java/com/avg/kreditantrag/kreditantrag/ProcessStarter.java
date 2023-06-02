package com.avg.kreditantrag.kreditantrag;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class ProcessStarter {
    private final String JOB_TYPE = "kreditantrag";

    @Qualifier("zeebeClientLifecycle")
    @Autowired
    private ZeebeClient client;

    private final static Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    @GetMapping
    public ResponseEntity<Void> onBoard() {
        try {
            client.newActivateJobsCommand().jobType(JOB_TYPE);
        } catch (Exception e) {
            LOGGER.error("Job: jobType={} failed to start with error: type={}, message={}",
                    JOB_TYPE,
                    e.getClass(),
                    e.getMessage());
            return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return status(HttpStatus.OK).build();
    }
}
