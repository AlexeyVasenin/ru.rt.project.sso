package ru.rt.cinema.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.cinema.domain.Movie;

import java.util.List;

@Component
public class RestRequestHandler {
    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    @Autowired
    private WebClient webClient;

    public List<Movie> requestToGetAllMovies() {
        List<Movie> movies = this.webClient.get()
                .uri(cinemaApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Movie>>() {
                })
                .block();

        if (movies != null) {
            movies.forEach(Movie::savePosterImageLocally);
        }

        return movies;
    }
}
