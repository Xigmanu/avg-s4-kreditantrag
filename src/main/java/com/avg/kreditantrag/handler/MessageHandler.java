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

@Component
@SuppressWarnings("unused")
public class MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient zeebeClient;

    @Autowired
    public MessageHandler(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "send-verification-email-message")
    public Map<String, Object> handleSendEmailMessageStart(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());

        final String messageName = "SendVerificationEmailMessage";
        final Map<String, Object> vars = job.getVariablesAsMap();

        int verificationCode = new Random().nextInt(100_000, 999_999);
        LOGGER.debug("Generated verification code");
        final String empId = vars.get("empId").toString();

        vars.put("verificationCode", verificationCode);

        LOGGER.info("Sending a message: name={}", messageName);
        zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(empId)
                .variables(
                        Map.of("empId", empId,
                                "prename", vars.get("prename"),
                                "email", vars.get("email"),
                                "verificationCode", verificationCode)
                )
                .send()
                .join();

        LOGGER.info("End handling job={}", job.getType());
        return vars;
    }

    @JobWorker(type = "notify-manager-message")
    public void handleNotifyManagerMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        String messageName = "CallTheManagerMessage";
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
                .messageName(messageName)
                .correlationKey(out.get("empId").toString())
                .variables(out)
                .send()
                .join();

        LOGGER.info("End handling job={}", job.getType());
    }

    @JobWorker(type = "send-email-to-manager")
    public void handleSendEmailToTheManager(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, Object> out = new HashMap<>();
        out.put("surname", vars.get("surname"));

        zeebeClient
                .newPublishMessageCommand()
                .messageName("SendMessageToManager")
                .correlationKey(vars.get("empId").toString())
                .variables(out)
                .send()
                .join();
        LOGGER.info("End handling job={}", job.getType());
    }

    @JobWorker(type = "send-confirmation-message")
    public void handleSendConfirmationEmail(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        sendSimpleMessage(job);
        LOGGER.info("End handling job={}", job.getType());
    }

    @JobWorker(type = "auto-approval-message")
    public void handleAutoApprovalMessage(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        sendSimpleMessage(job);
        LOGGER.info("End handling job={}", job.getType());
    }

    private void sendSimpleMessage(ActivatedJob job) {
        final String empId = job.getVariablesAsMap().get("empId").toString();
        final String messageName = "SendConfirmationEmailMessage";

        zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(empId)
                .send();
    }
}