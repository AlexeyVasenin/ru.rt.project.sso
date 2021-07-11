package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.cinema.handlers.RestRequestHandler;

import java.security.Principal;

@Controller
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping("/{id}")
    public String getMoviePage(@PathVariable Integer id, Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("movie", restRequestHandler.requestToGetMovieById(id));
        return "movie_page";
    }
}
