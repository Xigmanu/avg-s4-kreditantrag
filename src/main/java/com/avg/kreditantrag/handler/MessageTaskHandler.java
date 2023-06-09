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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.avg.kreditantrag.handler.HandlerConstants.APPROVAL_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.AUTO_APPROVAL_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.CONFIRMATION_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.EMAIL_TO_MANAGER_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.IN_PROCESS_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.MESSAGE_CORRELATION_KEY;
import static com.avg.kreditantrag.handler.HandlerConstants.NOTIFY_THE_MANAGER_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.REJECTION_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.TIMEOUT_EMAIL_MESSAGE;
import static com.avg.kreditantrag.handler.HandlerConstants.VERIFICATION_EMAIL_MESSAGE;

@Component
@SuppressWarnings({"Duplicates", "unused", "SpellCheckingInspection"})
public class MessageTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public MessageTaskHandler(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "send-verification-email-message")
    public Map<String, Object> handleSendEmailMessageStart(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String empId = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        LOGGER.debug("Get variables map from the task: type=[{}]", job.getType());
        final Map<String, Object> vars = job.getVariablesAsMap();

        LOGGER.debug("Generate a new verification code");
        int verificationCode = new Random().nextInt(100_000, 999_999);

        vars.put("verificationCode", verificationCode);

        LOGGER.info("Sending a message: name={}", VERIFICATION_EMAIL_MESSAGE);
        zeebeClient
                .newPublishMessageCommand()
                .messageName(VERIFICATION_EMAIL_MESSAGE)
                .correlationKey(empId)
                .variables(
                        Map.of("empId", empId,
                                "prename", vars.get("prename"),
                                "email", vars.get("email"),
                                "verificationCode", verificationCode)
                )
                .send()
                .join();

        LOGGER.info("Exit Handling job: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        return vars;
    }

    @JobWorker(type = "notify-manager-message")
    public void handleNotifyManagerMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String key = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> out = new HashMap<>();

        out.put("empId", vars.get("empId"));
        out.put("prename", vars.get("prename"));
        out.put("email", vars.get("email"));
        out.put("reportSum", vars.get("reportSum"));
        out.put("date", vars.get("date"));
        out.put("description", vars.get("description"));

        zeebeClient
                .newPublishMessageCommand()
                .messageName(NOTIFY_THE_MANAGER_MESSAGE)
                .correlationKey(key)
                .variables(out)
                .send()
                .join();

        LOGGER.info("Exit Handling job: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-email-to-manager")
    public void handleSendEmailToTheManager(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String key = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> out = new HashMap<>();
        out.put("surname", vars.get("surname"));

        zeebeClient
                .newPublishMessageCommand()
                .messageName(EMAIL_TO_MANAGER_MESSAGE)
                .correlationKey(key)
                .variables(out)
                .send()
                .join();

        LOGGER.info("Exit Handling job: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-manual-approval-email")
    public void handleSendApprovalEmail(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, APPROVAL_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }
    @JobWorker(type = "send-manual-rejection-email")
    public void handleSendRejectionEmail(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, REJECTION_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-in-process-email")
    public void handleSendInProcessEmail(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, IN_PROCESS_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-resubmission-request-email")
    public void handleSendResubmissionRequestMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, TIMEOUT_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-confirmation-email")
    public void handleSendConfirmationEmail(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, CONFIRMATION_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    @JobWorker(type = "send-auto-approval-email")
    public void handleAutoApprovalMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Handling task: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());

        sendSimpleMessage(job, AUTO_APPROVAL_EMAIL_MESSAGE);

        LOGGER.info("Exit task handler: type=[{}], bpmnProcessId=[{}]",
                job.getType(), job.getBpmnProcessId());
    }

    private void sendSimpleMessage(ActivatedJob job, String messageName) {
        LOGGER.debug("Get the correlation key from task: type=[{}]", job.getType());
        final String key = job
                .getVariablesAsMap()
                .get(MESSAGE_CORRELATION_KEY)
                .toString();

        zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(key)
                .send();

    }
}
