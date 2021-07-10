package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.cinema.handlers.RestRequestHandler;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping("/{id}")
    public String getMoviePage(@PathVariable Integer id, Model model) {
        // TODO сделать запрос к resource server и получить информацию о фильме по его id -> добавить ее в модель

        return "movie_page";
    }
}
