package com.avg.kreditantrag.handler;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.process.VerificationController;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@SuppressWarnings("unused")
public class VerificationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    @JobWorker(type = "verification-code-check")
    public void handleVerificationCodeCheck(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        Map<String, Object> vars = job.getVariablesAsMap();

        String verificationCode = vars.get("verificationCode").toString();
        String userCode;
        while (true) {
            if (VerificationController.userCodeInput != null) {
                userCode = VerificationController.userCodeInput;
                break;
            }
        }
        LOGGER.info("Compare: ref={} with in={}", verificationCode, userCode);

        if (!Objects.equals(verificationCode, userCode)) {
            client.newThrowErrorCommand(job)
                    .errorCode("verification_failed")
                    .errorMessage("Verification failed")
                    .send();
        }
    }
}
