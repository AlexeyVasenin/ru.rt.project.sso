package ru.rt.cinema.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.cinema.domain.Movie;
import ru.rt.cinema.services.ImageLocalHandleService;
import ru.rt.cinema.services.UserDetailsCollectorService;

import java.util.List;

/**
 * Обработчик запросов к ресурс-серверу, реализующий {@link RestRequestHandler}
 * <p>
 *
 * @author Alexey Baidin
 */
@Component
public class RestRequestHandlerImpl implements RestRequestHandler {

    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    private final WebClient webClient;

    private final ImageLocalHandleService imageLocalHandleService;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient, ImageLocalHandleService imageLocalHandleService) {
        this.webClient = webClient;
        this.imageLocalHandleService = imageLocalHandleService;
    }

    private List<Movie> getAllMovies() {
        return this.webClient.get()
                .uri(cinemaApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Movie>>() {
                })
                .block();
    }

    public List<Movie> requestToGetAllMovies() {
        return this.getAllMovies();
    }

    @Override
    public List<Movie> requestToGetAllMoviesAndSaveLocally() {
        List<Movie> movies = this.getAllMovies();

        movies.forEach(imageLocalHandleService::savePosterImageLocally);

        return movies;
    }

    public Movie requestToGetMovieById(Integer id) {
        return this.webClient.get()
                .uri(cinemaApiUrl + "/" + id)
                .retrieve()
                .bodyToMono(Movie.class)
                .block();
    }
}
