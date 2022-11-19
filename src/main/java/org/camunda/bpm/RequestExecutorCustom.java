package org.camunda.bpm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.camunda.bpm.client.impl.RequestExecutor;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public class RequestExecutorCustom extends RequestExecutor {

    private HttpRequestRetryHandler handler;

    public RequestExecutorCustom(RequestInterceptorHandler requestInterceptorHandler, ObjectMapper objectMapper, HttpRequestRetryHandler handler, HttpClient httpClient) {
        super(requestInterceptorHandler, objectMapper);
        this.handler = handler;
        this.httpClient = httpClient;
    }

    @Override
    public void initHttpClient(RequestInterceptorHandler requestInterceptorHandler) {
        // do nothing
    }
}
