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

/**
 * A service class that sends HTTP requests to the target server to manage employee data.
 */
@Service
public class EmployeeService implements KreditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KreditantragApplication.class);
    private static final String ENDPOINT = "employees";
    private final OkHttpClient httpClient;

    /**
     * Initializes a new instance of {@code EmployeeService} with an object of type
     * {@code OkHttpClient} as parameter that is injected.
     *
     * @param httpClient client that sends HTTP requests
     */
    @Autowired
    public EmployeeService(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Response getById(int id) throws IOException {
        LOGGER.info("EmployeeService: getById: id={}", id);
        return getGetResponse(ENDPOINT,
                id,
                null,
                null,
                httpClient);
    }

    @Override
    public Response create(Object employee) throws IOException {
        LOGGER.info("EmployeeService: create: employee={}", employee);
        return getPostResponse(ENDPOINT,
                serialize(employee),
                httpClient);
    }
}
