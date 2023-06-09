package com.avg.kreditantrag.handler;

import com.avg.kreditantrag.KreditantragApplication;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.avg.kreditantrag.handler.HandlerConstants.EMAIL_ERROR_TO_MANAGER_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.MESSAGE_CORRELATION_KEY;

@Component
@SuppressWarnings("unused")
public class ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public ErrorHandler(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "inform-manager-about-error")
    public void handleInformManagerAboutError(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String key = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        zeebeClient
                .newPublishMessageCommand()
                .messageName(EMAIL_ERROR_TO_MANAGER_MESSAGE)
                .correlationKey(key)
                .variables(Map.of("date", new Date()))
                .send();

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }
}
