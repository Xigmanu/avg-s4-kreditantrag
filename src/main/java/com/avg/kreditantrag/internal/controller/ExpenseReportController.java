package com.avg.kreditantrag.internal.controller;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.entity.ExpenseReport;
import com.avg.kreditantrag.internal.service.ExpenseReportService;
import com.avg.kreditantrag.internal.service.HttpStatus;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.avg.kreditantrag.internal.helper.JsonMapper.deserialize;

/**
 * Controller class that communicates with the target server using HTTP requests to manage expense report data.
 */
@Controller
public class ExpenseReportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ExpenseReportService service;

    /**
     * Initializes a new instance of {@code ExpenseReportController} class with an injected instance
     * of type {@code ExpenseReportService} as parameter.
     *
     * @param service service class with methods to send HTTP requests to the target server.
     */
    @Autowired
    public ExpenseReportController(ExpenseReportService service) {
        this.service = service;
    }

    /**
     * Sends a GET request to the target server to retrieve expense report data.
     *
     * @param employeeId id of an employee
     * @return a list with objects of type {@code ExpenseReport} class on success, else {@code null}
     */
    public List<ExpenseReport> getReportsForEmployee(int employeeId) {
        LOGGER.info("ExpenseReportController: getReportsForEmployee: employeeId={}", employeeId);

        try {
            Response response = service.getById(employeeId);
            if (response.code() == HttpStatus.NOT_FOUND.getCode()) {
                LOGGER.info("ExpenseReportController: getReportsForEmployee: No reports for the employee with id={}", employeeId);
                return null;
            }
            String json = response.body().string();
            LOGGER.info("ExpenseReportController: getReportsForEmployee: responseCode=[{}]", response.code());

            return Arrays.stream(deserialize(json, ExpenseReport[].class)).toList();
        } catch (IOException ioe) {
            LOGGER.error("EmployeeController: getById: An exception of type [{}] was thrown. Message={}",
                    ioe.getClass().getSimpleName(), ioe.getMessage());
            return null;
        }
    }

    /**
     * Sends a POST request to the target server with new expense report data.
     *
     * @param report new expense report data to send to the server
     * @return enum with an HTTP response code
     */
    public HttpStatus create(ExpenseReport report) {
        LOGGER.info("ExpenseReportController: create: report={}", report);
        try {
            Response response = service.create(report);

            if (response.isSuccessful()) {
                LOGGER.info("ExpenseReportController: create: Successfully created a report: {}", report);
                return HttpStatus.CREATED;
            } else {
                LOGGER.warn("ExpenseReportController: create: Could not create a report: {}", report);
                return HttpStatus.BAD_REQUEST;
            }
        } catch (IOException ioe) {
            LOGGER.error("ExpenseReportController: create: An exception of type [{}] was thrown. Message: {}",
                    ioe.getClass().getSimpleName(), ioe.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
