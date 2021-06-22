package ru.rt.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Optional;

@Controller
public class WebController {

    private final Environment environment;

    @Autowired
    public WebController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public String index(Model model) {
        Optional<String> serviceName = Arrays.stream(environment.getActiveProfiles()).findFirst();

        if (serviceName.isPresent()) {
            model.addAttribute("serviceName", serviceName.get());
        } else {
            model.addAttribute("serviceName", "Unknown");
        }

        return "index";
    }
}
