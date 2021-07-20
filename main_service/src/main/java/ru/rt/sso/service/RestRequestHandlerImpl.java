package ru.rt.sso.service;

import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String eurekaURL;

    private final WebClient webClient;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Application> getAllApplications() {
        String allApplicationUri = eurekaURL.replace("eureka/", "api/services");

        return this.webClient.get()
                .uri(allApplicationUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Application>>() {
                })
                .block();
    }
}
