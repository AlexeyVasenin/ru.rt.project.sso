package ru.rt.cinema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.cinema.handlers.RestRequestHandler;

import java.security.Principal;

/**
 * Web-контроллер для обработки запросов, связанных с фильмами (например, получения страницы фильма по его id).
 * <p>
 *
 * @author Alexey Baidin
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

    private final RestRequestHandler restRequestHandler;

    @Autowired
    public MovieController(RestRequestHandler restRequestHandler) {
        this.restRequestHandler = restRequestHandler;
    }

    @GetMapping("/{id}")
    public String getMoviePage(@PathVariable Integer id, Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("movie", restRequestHandler.requestToGetMovieById(id));
        return "movie_page";
    }
}
