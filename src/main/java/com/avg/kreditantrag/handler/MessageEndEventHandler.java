package com.avg.kreditantrag.handler;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.process.ProcessStartController;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class MessageEndEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public MessageEndEventHandler(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "send-timeout-end-message")
    public void handleSendTimeoutEndMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling job: type={}", job.getType());

        sendSimpleMessage("TimeoutMessage", job
                .getVariablesAsMap()
                .get("empId")
                .toString());

        LOGGER.info("Exit Handling job: type={}", job.getType());
    }

    @JobWorker(type = "send-approval-end-message")
    public void handleSendApprovalEndMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling job: type={}", job.getType());

        sendSimpleMessage("ManualApprovedMessage", job
                .getVariablesAsMap()
                .get("empId")
                .toString());

        LOGGER.info("Exit Handling job: type={}", job.getType());
    }

    @JobWorker(type = "send-rejection-end-message")
    public void handleSendRejectionEndMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling job: type={}", job.getType());

        sendSimpleMessage("ManualRejectedMessage", job
                .getVariablesAsMap()
                .get("empId")
                .toString());

        LOGGER.info("Exit Handling job: type={}", job.getType());
    }

    private void sendSimpleMessage(String messageName, String correlationKey) {
        zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .send()
                .join();

        zeebeClient
                .newPublishMessageCommand()
                .messageName("TerminateMailService")
                .correlationKey(correlationKey)
                .send()
                .join();
    }
}
