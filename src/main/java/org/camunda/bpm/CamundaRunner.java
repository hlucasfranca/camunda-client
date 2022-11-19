package org.camunda.bpm;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CamundaRunner {

    private HttpRequestRetryHandler handler = (e, i, httpContext) -> false;

    @Autowired
    private HttpClient httpClient;


    public void run() {

        ExternalTaskClient client = ExternalTaskClientCustom
                .create(handler, httpClient)
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(1000)
                .build();
        try {
            client.subscribe("invoiceCreator")
                    .handler((externalTask, externalTaskService) -> {

                        // instantiate an invoice object
                        Invoice invoice = new Invoice("A123");

                        // create an object typed variable with the serialization format XML
                        ObjectValue invoiceValue = ClientValues
                                .objectValue(invoice)
                                .serializationDataFormat("application/xml")
                                .create();

                        // add the invoice object and its id to a map
                        Map<String, Object> variables = new HashMap<>();
                        variables.put("invoiceId", invoice.id);
                        variables.put("invoice", invoiceValue);

                        // select the scope of the variables
                        boolean isRandomSample = Math.random() <= 0.5;
                        if (isRandomSample) {
                            externalTaskService.complete(externalTask, variables);
                        } else {
                            externalTaskService.complete(externalTask, null, variables);
                        }

                        System.out.println("The External Task " + externalTask.getId() +
                                " has been completed!");

                    }).open();
        } catch (EngineConnectionException e) {
            e.printStackTrace();
        }
    }
}
