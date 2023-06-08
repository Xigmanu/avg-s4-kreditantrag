package com.avg.kreditantrag.handler;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.controller.EmployeeController;
import com.avg.kreditantrag.internal.entity.Employee;
import com.avg.kreditantrag.internal.service.HttpStatus;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@SuppressWarnings("unused")
public class EmployeeHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final EmployeeController employeeController;
    private final ZeebeClient zeebeClient;

    @Autowired
    public EmployeeHandler(EmployeeController employeeController,
                           @Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.employeeController = employeeController;
        this.zeebeClient = zeebeClient;
    }

    @JobWorker(type = "account-check")
    public Map<String, Object> handleAccountCheck(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());
        Map<String, Object> vars = job.getVariablesAsMap();

        int empId = Integer.parseInt(vars.get("empId").toString());
        boolean exists = employeeController.getById(empId) != null;

        vars.put("exists", exists);
        LOGGER.info("End handling job={}", job.getType());
        return vars;
    }

    @JobWorker(type = "account-create")
    public void handleCreateAccount(JobClient client, ActivatedJob job) {
        LOGGER.info("Start handling job={}", job.getType());

        Map<String, Object> vars = job.getVariablesAsMap();

        Employee newEmp = Employee.builder()
                .prename(vars.get("prename").toString())
                .surname(vars.get("surname").toString())
                .build();

        if (employeeController.create(newEmp) == HttpStatus.CREATED) {
            LOGGER.info("Successfully created a new employee");
            return;
        }

        LOGGER.error("Connection to the database refused");
        client.newThrowErrorCommand(job)
                .errorCode("db_connection_error")
                .errorMessage("Refused connection to db")
                .send();
    }
}
