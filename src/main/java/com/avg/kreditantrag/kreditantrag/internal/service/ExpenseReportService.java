package com.avg.kreditantrag.kreditantrag.internal.service;

import com.avg.kreditantrag.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.kreditantrag.internal.entity.ExpenseReport;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.avg.kreditantrag.kreditantrag.internal.helper.JsonMapper.serialize;
import static com.avg.kreditantrag.kreditantrag.internal.helper.UrlHelper.getUrl;

@Service
public class ExpenseReportService implements KreditService{
    private final String ENDPOINT = "expense_reports";
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final OkHttpClient httpClient;

    public ExpenseReportService() {
        httpClient = new OkHttpClient();
    }

    public Response getById(int id) throws IOException {
        LOGGER.info("ExpenseReportService: getById: id={}", id);
        return getGetResponse(getUrl(ENDPOINT, id), httpClient);
    }

    public Response create(ExpenseReport report) throws IOException {
        LOGGER.info("ExpenseReportService: create: report={}", report);
        return getPostResponse(ENDPOINT, serialize(report), httpClient);
    }
}
