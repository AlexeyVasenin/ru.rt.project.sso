package ru.rt.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cinema")
public class CinemaController {

    private final List<Movie> movies = new ArrayList<>() {{
        add(new Movie(1L, "Shrek 2", 2004L));
        add(new Movie(2L, "Pulp Fiction", 1994L));
    }};

    @GetMapping
    public Collection<Movie> findAll() {
        return movies;
    }
}
