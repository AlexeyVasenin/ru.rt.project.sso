package ru.rt.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import ru.rt.cinema.handlers.RestRequestHandler;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
public class MicroserviceCinemaApplication {

//    @Autowired
//    private RestRequestHandler restRequestHandler;

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceCinemaApplication.class, args);
    }

//    @PostConstruct
//    private void postConstruct() {
//        // локальная загрузка изображений с сервера ресурсов
//        restRequestHandler.requestToGetAllMovies();
//    }

}
