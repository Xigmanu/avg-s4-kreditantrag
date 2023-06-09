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

import static com.avg.kreditantrag.handler.HandlerConstants.MANUAL_APPROVED_TERMINATION_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.MANUAL_REJECTED_TERMINATION_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.MESSAGE_CORRELATION_KEY;
import static com.avg.kreditantrag.handler.HandlerConstants.TERMINATE_MAIL_SERVICE_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.TIMEOUT_TERMINATION_MESSAGE;

@Component
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class MessageEndEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public MessageEndEventHandler(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "send-timeout-end-message")
    public void handleSendTimeoutEndMessage(JobClient client, ActivatedJob job) {
        handleGracefulProcessTermination(job, TIMEOUT_TERMINATION_MESSAGE);
    }

    @JobWorker(type = "send-approval-end-message")
    public void handleSendApprovalEndMessage(JobClient client, ActivatedJob job) {
        handleGracefulProcessTermination(job, MANUAL_APPROVED_TERMINATION_MESSAGE);
    }

    @JobWorker(type = "send-rejection-end-message")
    public void handleSendRejectionEndMessage(JobClient client, ActivatedJob job) {
        handleGracefulProcessTermination(job, MANUAL_REJECTED_TERMINATION_MESSAGE);
    }

    private void handleGracefulProcessTermination(ActivatedJob job, String manualRejectedTerminationMessage) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String key = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        sendReviewStatus(key, manualRejectedTerminationMessage);
        sendProcessTerminationMessage(key);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }
    private void sendProcessTerminationMessage(String correlationKey) {
        LOGGER.info("Sending a message to terminate system-mail process with correlationKey=[{}]", correlationKey);

        zeebeClient
                .newPublishMessageCommand()
                .messageName(TERMINATE_MAIL_SERVICE_MESSAGE)
                .correlationKey(correlationKey)
                .send()
                .join();

        LOGGER.info("Successfully sent a message: correlationKey=[{}]", correlationKey);
    }
    private void sendReviewStatus(String correlationKey, String messageName) {
        LOGGER.info("Sending a message: name=[{}], correlationKey=[{}]",
                messageName, correlationKey);

        zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .send()
                .join();

        LOGGER.info("Successfully sent a message: name=[{}], correlationKey=[{}]",
                messageName, correlationKey);
    }
}
