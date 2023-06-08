package com.avg.kreditantrag.handler;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.controller.ExpenseReportController;
import com.avg.kreditantrag.internal.entity.ExpenseReport;
import com.avg.kreditantrag.internal.service.HttpStatus;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unused")
public class ExpenseReportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ExpenseReportController expenseReportController;

    @Autowired
    public ExpenseReportHandler(ExpenseReportController expenseReportController) {
        this.expenseReportController = expenseReportController;
    }

    @JobWorker(type = "expense-report-get-count")
    public Map<String, Object> handleExpenseReportGetCount(JobClient client, ActivatedJob job) {
        final Map<String, Object> vars = job.getVariablesAsMap();
        final int empId = Integer.parseInt(vars.get("empId").toString());
        final List<ExpenseReport> expenseReports = expenseReportController.getReportsForEmployee(empId);
        final Map<String, Object> out = new HashMap<>();

        out.put("reportCount", (long) expenseReports.size());

        return out;
    }

    @JobWorker(type = "expense-report-save")
    public void handleExpenseReportCreate(JobClient client, ActivatedJob job) {
        final Map<String, Object> vars = job.getVariablesAsMap();

        final ExpenseReport newReport = ExpenseReport.builder()
                .employee_id(
                        Integer.parseInt(vars.get("empId").toString()))
                .date(new Date().toString())
                .sum(Double.parseDouble(vars.get("reportSum").toString()))
                .description(vars.get("description").toString())
                .build();

        if (expenseReportController.create(newReport) == HttpStatus.CREATED) {
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
