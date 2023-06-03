package com.avg.kreditantrag.internal.controller;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.entity.ExpenseReport;
import com.avg.kreditantrag.internal.service.ExpenseReportService;
import com.avg.kreditantrag.internal.service.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import scala.NotImplementedError;

@Controller
public class ExpenseReportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final ExpenseReportService service;

    @Autowired
    public ExpenseReportController(ExpenseReportService service) {
        this.service = service;
    }

    public ExpenseReport getById(int id) {
        throw new NotImplementedError();
    }
    public HttpStatus create(ExpenseReport report) {
        throw new NotImplementedError();
    }
}
