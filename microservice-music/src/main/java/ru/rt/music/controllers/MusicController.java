package ru.rt.music.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/music")
public class MusicController {

    @GetMapping
    public String getTokenDetails(@RequestHeader HttpHeaders headers) {
        return headers.toString();
    }
}
