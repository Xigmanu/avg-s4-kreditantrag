package com.avg.kreditantrag.internal.controller;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.entity.Employee;
import com.avg.kreditantrag.internal.service.EmployeeService;
import com.avg.kreditantrag.internal.service.HttpStatus;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static com.avg.kreditantrag.internal.helper.JsonMapper.deserialize;

/**
 * Controller class that communicates with the target server using HTTP requests to manage employee data.
 */
@Controller
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final EmployeeService service;

    /**
     * Initializes a new instance of {@code EmployeeController} class with an injected instance
     * of type {@code EmployeeService} as parameter.
     *
     * @param service service class with methods to send HTTP requests to the target server.
     */
    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /**
     * Sends a GET request to the target server to retrieve employee data.
     *
     * @param id id of an employee
     * @return an instance of {@code Employee} class on success, else {@code null}
     */
    public Employee getById(int id) {
        LOGGER.info("EmployeeController: getById: id={}", id);
        try {
            Response response = service.getById(id);
            if (response.code() == HttpStatus.NOT_FOUND.getCode()) {
                LOGGER.info("EmployeeController: getById: An employee with the id [{}] does not exist", id);
                return null;
            }
            String json = response.body().string();
            LOGGER.info("EmployeeController: getById: responseCode=[{}]", response.code());

            return deserialize(json, Employee.class);
        } catch (IOException ioe) {
            LOGGER.error("EmployeeController: getById: An exception of type [{}] was thrown. Message={}",
                    ioe.getClass().getSimpleName(), ioe.getMessage());
            return null;
        }
    }

    /**
     * Sends a POST request to the target server with new employee data.
     *
     * @param employee new employee data to send to the server
     * @return enum with an HTTP response code
     */
    public HttpStatus create(Employee employee) {
        LOGGER.info("EmployeeController: create: employee={}", employee);

        try {
            if (getById(employee.getId()) != null) {
                LOGGER.info("EmployeeController: create: An employee with the id [{}] already exists", employee.getId());
                return HttpStatus.CONFLICT;
            }

            Response response = service.create(employee);

            if (response.isSuccessful()) {
                LOGGER.info("EmployeeController: create: Successfully created an employee: {}", employee);
                return HttpStatus.CREATED;
            } else {
                LOGGER.warn("EmployeeController: create: Could not create an employee: {}", employee);
                return HttpStatus.BAD_REQUEST;
            }
        } catch (IOException ioe) {
            LOGGER.error("EmployeeController: create: An exception of type [{}] was thrown. Message: {}",
                    ioe.getClass().getSimpleName(), ioe.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
