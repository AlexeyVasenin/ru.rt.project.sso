package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping("/{id}")
    public String getMoviePage(@PathVariable Integer id, Model model) {
        // TODO сделать запрос к resource server и получить информацию о фильме по его id -> добавить ее в модель

        return "movie_page";
    }
}
