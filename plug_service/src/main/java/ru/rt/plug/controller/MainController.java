package ru.rt.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class MainController {
    // Ubuntu: ip a (5: inet ip)
    private final String authorityIP = "172.27.100.22";
    private final String URL = "http://" + authorityIP + ":8080/api";

    private final RestTemplate restTemplate;

    private final Environment environment;

    @Autowired
    public MainController(Environment environment) {
        this.environment = environment;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/user/{userId}/role/{roleName}")
    public ResponseEntity<String> setNewRoleToUser(@PathVariable String userId, @PathVariable String roleName) throws URISyntaxException {
        String query = String.format("/user/%s/role/%s/service/%s", userId, roleName,
                Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("error"));
        String result = restTemplate.postForObject(new URI(URL + query), null, String.class);

        System.out.println("Plug service: " + result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
