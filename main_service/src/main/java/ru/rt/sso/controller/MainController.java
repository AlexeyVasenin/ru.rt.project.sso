package ru.rt.sso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping("/user/{userId}/role/{roleName}/service/{serviceName}")
    public ResponseEntity<String> setNewRoleToUser(@PathVariable String userId, @PathVariable String roleName, @PathVariable String serviceName) {
        String result = String.format("User %s now has role %s in %s", userId, roleName, serviceName);

        System.out.println("Main service: " + result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
