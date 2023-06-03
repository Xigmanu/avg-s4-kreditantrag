package com.avg.kreditantrag.kreditantrag.internal.controller;

import com.avg.kreditantrag.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.kreditantrag.internal.entity.Employee;
import com.avg.kreditantrag.kreditantrag.internal.service.EmployeeService;
import com.avg.kreditantrag.kreditantrag.internal.service.HttpStatus;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static com.avg.kreditantrag.kreditantrag.internal.helper.JsonMapper.deserialize;

@Controller
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    public Employee getById(int id) {
        LOGGER.info("EmployeeController: getById: id={}", id);

        try {
            Response response = service.getById(id);
            if (response.code() == HttpStatus.NOT_FOUND.getCode()) {
                LOGGER.info("EmployeeController: getById: An employee with the id [{}] does not exist", id);
                return null;
            }
            String json = response.body().string();
            LOGGER.info("EmployeeController: getById: response={}", json);

            return deserialize(json, Employee.class);
        } catch (IOException ioe) {
            LOGGER.error("EmployeeController: getById: An exception of type [{}] was thrown. Message={}",
                    ioe.getClass().getSimpleName(), ioe.getMessage());
            return null;
        }
    }

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
