package ru.rt.sso.service;

import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Обработчик запросов к Eureka-серверу, реализующий {@link RestRequestHandler}
 * <p>
 *
 * @author Alexey Baidin
 */
@Component
public class RestRequestHandlerImpl implements RestRequestHandler {

    private final String eurekaURL = "http://eureka-server-java-school.apps.okd.stage.digital.rt.ru/api/services";
//    private final String eurekaURL = "http://localhost:8761/api/services";

    private final WebClient webClient;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Application> getAllApplications() {
        return this.webClient.get()
                .uri(eurekaURL)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Application>>() {
                })
                .block();
    }
}
