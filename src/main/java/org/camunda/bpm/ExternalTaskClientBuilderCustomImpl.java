package org.camunda.bpm;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.camunda.bpm.client.impl.EngineClient;
import org.camunda.bpm.client.impl.ExternalTaskClientBuilderImpl;
import org.camunda.bpm.client.impl.RequestExecutor;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public class ExternalTaskClientBuilderCustomImpl extends ExternalTaskClientBuilderImpl {

    private HttpRequestRetryHandler handler;

    private HttpClient httpClient;

    public ExternalTaskClientBuilderCustomImpl(HttpRequestRetryHandler handler, HttpClient httpClient){
        super();
        this.handler = handler;
        this.httpClient = httpClient;
    }

    @Override
    protected void initEngineClient() {
        RequestInterceptorHandler requestInterceptorHandler = new RequestInterceptorHandler(this.interceptors);
        RequestExecutor requestExecutor = new RequestExecutorCustom(requestInterceptorHandler, this.objectMapper, this.handler, this.httpClient);
        this.engineClient = new EngineClient(this.workerId, this.maxTasks, this.asyncResponseTimeout, this.baseUrl, requestExecutor);
    }

}
