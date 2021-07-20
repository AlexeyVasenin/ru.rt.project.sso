package ru.rt.sso.service;

import com.netflix.discovery.shared.Application;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы обработки запросов к Eureka-серверу.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface RestRequestHandler {

    /**
     * Запрос к серверу ресурсов на получение всех приложений.
     * <p>
     *
     * @return список Application
     */
    List<Application> getAllApplications();
}
