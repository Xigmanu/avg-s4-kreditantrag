package com.avg.kreditantrag.internal.service;

import com.avg.kreditantrag.KreditantragApplication;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.avg.kreditantrag.internal.helper.JsonMapper.serialize;

@Service
public class ExpenseReportService implements KreditService{
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final String ENDPOINT = "expense_reports";
    private final OkHttpClient httpClient;

    @Autowired
    public ExpenseReportService(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Response getById(int id) throws IOException {
        LOGGER.info("ExpenseReportService: getById: id={}", id);
        return getGetResponse(ENDPOINT,
                null,
                "employee_id",
                id,
                httpClient);
    }

    @Override
    public Response create(Object report) throws IOException {
        LOGGER.info("ExpenseReportService: create: report={}", report);
        return getPostResponse(ENDPOINT,
                serialize(report),
                httpClient);
    }
}
