package com.avg.kreditantrag.kreditantrag.internal.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.avg.kreditantrag.kreditantrag.internal.helper.UrlHelper.getUrl;

public interface KreditService {
    default Response getGetResponse(String url, OkHttpClient httpClient) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return httpClient.newCall(request).execute();
    }
    default Response getPostResponse(String endpoint,
                                 String json,
                                 OkHttpClient httpClient) throws IOException {
        String url = getUrl(endpoint, null);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return httpClient.newCall(request).execute();
    }
}
