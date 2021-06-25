package ru.rt.cinema.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api")
public class CinemaController {

    @GetMapping("/cinema")
    public String getTokenDetails(@RequestHeader HttpHeaders headers) {
        return headers.toString();
    }

}
