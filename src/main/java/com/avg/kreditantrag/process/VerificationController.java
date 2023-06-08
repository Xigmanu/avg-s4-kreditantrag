package com.avg.kreditantrag.process;

import com.avg.kreditantrag.KreditantragApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class VerificationController {
    public static String userCodeInput;
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);

    @PostMapping("/success")
    public void start(@RequestBody Map<String, Object> vars) {
        LOGGER.info("Received a verification code from user");
        userCodeInput = vars.get("verificationCode").toString();
    }
}
