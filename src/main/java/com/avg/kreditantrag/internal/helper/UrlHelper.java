package com.avg.kreditantrag.internal.helper;

public class UrlHelper {
    private static final String BASE_URI = "http://localhost:3000";
    public static String getUrl(String endpoint, Object pathVariable) {
        return String.format("%s/%s%s",
                BASE_URI,
                endpoint,
                pathVariable != null ? "/" + pathVariable : "");
    }
}
