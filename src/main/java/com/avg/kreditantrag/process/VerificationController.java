package com.avg.kreditantrag.process;

import com.avg.kreditantrag.KreditantragApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class VerificationController {
    public static String exists;
    public static String userCodeInput;
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    @PostMapping("/verify")
    public void start(@RequestBody Map<String, Object> vars) {
        LOGGER.info("Received a verification code from user");
        userCodeInput = vars.get("verificationCode").toString();
    }
    @GetMapping("/exists")
    public String exists() {
        try {
            while (true) {
                LOGGER.info("Received a string from handler");
                if (Objects.equals(exists, "true") || Objects.equals(exists, "false")) {
                    LOGGER.info("Received a string from handler");
                    return exists;
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException occurred", e);
            return "error";
        }
    }
}
