package ru.rt.cinema.microservicecinema.controllers;


import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpHeaders;

@RestController
public class CinemaController {

    @RequestMapping(method = RequestMethod.GET, value = "/api/cinema/")
    public String getTokenDetails(@RequestHeader HttpHeaders headers) {
        return headers.toString();
    }
}
