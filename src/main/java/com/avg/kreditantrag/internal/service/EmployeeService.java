package com.avg.kreditantrag.internal.service;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.entity.Employee;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.avg.kreditantrag.internal.helper.JsonMapper.serialize;
import static com.avg.kreditantrag.internal.helper.UrlHelper.getUrl;

@Service
public class EmployeeService implements KreditService {
    private final String ENDPOINT = "employees";
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private final OkHttpClient httpClient; //TODO Implement caching to avoid unnecessary network overload

    public EmployeeService() {
        httpClient = new OkHttpClient();
    }

    public Response getById(int id) throws IOException {
        LOGGER.info("EmployeeService: getById: id={}", id);
        return getGetResponse(getUrl(ENDPOINT, id), httpClient);
    }

    public Response create(Employee employee) throws IOException {
        LOGGER.info("EmployeeService: create: employee={}", employee);
        return getPostResponse(ENDPOINT, serialize(employee), httpClient);
    }
}
