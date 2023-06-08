/*
----------------------*** WIP ***----------------------
 */

package com.avg.kreditantrag.process;

import com.avg.kreditantrag.KreditantragApplication;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class ProcessStartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ZeebeClient client;

    @Autowired
    public ProcessStartController(@Qualifier("zeebeClientLifecycle") ZeebeClient client) {
        this.client = client;
    }

    @PostMapping("/start")
    public void startKreditantragProcess(@RequestBody Map<String, Object> variables) {
        LOGGER.info("Starting process");

        ProcessInstanceEvent processInstanceEvent = client
                .newCreateInstanceCommand()
                .bpmnProcessId("employee-process")
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        LOGGER.info("Started: processInstanceKey={}, bpmnProcessId={}",
                processInstanceEvent.getProcessInstanceKey(),
                processInstanceEvent.getBpmnProcessId());

        client.newCreateInstanceCommand()
                .bpmnProcessId("system-intern")
                .latestVersion()
                .variables(variables)
                .send()
                .join();
    }
}
