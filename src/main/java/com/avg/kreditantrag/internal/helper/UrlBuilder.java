package com.avg.kreditantrag.internal.helper;

/**
 * A builder class that constructs a URL for HTTP requests.
 */
public class UrlBuilder {
    private static final String BASE_URI = "http://localhost:3000";
    private String endpoint;
    private Object pathVariable;
    private String queryParameter;
    private Object queryParameterValue;

    public UrlBuilder pathVariable(Object pathVariable) {
        if (pathVariable != null) {
            this.pathVariable = pathVariable;
        }
        return this;
    }
    public UrlBuilder endpoint(String endpoint) {
        if (endpoint != null) {
            this.endpoint = endpoint;
        }
        return this;
    }
    public UrlBuilder queryParameter(String queryParameter, Object value) {
        if (queryParameter != null && value != null) {
            this.queryParameter = queryParameter;
            queryParameterValue = value;
        }

        return this;
    }
    public String build() {
        StringBuilder strBuilder = new StringBuilder().append(BASE_URI);

        if (endpoint != null) {
            strBuilder.append('/').append(endpoint);
        }
        if (pathVariable != null) {
            strBuilder.append('/').append(pathVariable);
        }
        if (queryParameter != null) {
            strBuilder.append('?').append(queryParameter).append('=').append(queryParameterValue);
        }

        return strBuilder.toString();
    }
}
