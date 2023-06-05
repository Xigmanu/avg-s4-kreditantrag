package com.avg.kreditantrag.internal.service;

import com.avg.kreditantrag.KreditantragApplication;
import com.avg.kreditantrag.internal.helper.UrlBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Interface containing methods for service classes to use in order to send HTTP requests to target server.
 */
public interface KreditService {
    default Response getGetResponse(String endpoint,
                                    Object pathVariable,
                                    String queryParameter,
                                    Object queryParameterValue,
                                    OkHttpClient httpClient) throws IOException {
        UrlBuilder builder = new UrlBuilder();
        String url = builder
                .endpoint(endpoint)
                .pathVariable(pathVariable)
                .queryParameter(queryParameter, queryParameterValue)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return httpClient.newCall(request).execute();
    }
    default Response getPostResponse(String endpoint,
                                 String json,
                                 OkHttpClient httpClient) throws IOException {
        UrlBuilder builder = new UrlBuilder();
        String url = builder.endpoint(endpoint).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }

    /**
     * Sends a GET request to retrieve data by its id.
     *
     * @param id id of data entry
     * @return a response from the target server
     * @throws IOException if a request fails
     */
    Response getById(int id) throws IOException;

    /**
     * Sends a POST request with data in request body to the target server.
     *
     * @param obj data to send
     * @return a response from the target server
     * @throws IOException if a request fails
     */
    Response create(Object obj) throws IOException;
}
