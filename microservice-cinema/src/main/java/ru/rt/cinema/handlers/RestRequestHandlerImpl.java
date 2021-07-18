package ru.rt.cinema.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.cinema.domain.Movie;

import java.util.List;

@Component
//todo A. Baidin описание класса, методов --> доступ в интерйефс, реализация тут.
// Работаем с представлением, а не реализацией доки также в интерефейс
public class RestRequestHandlerImpl implements RestRequestHandler {

    @Value("${resource-server.api.url}")
    private String cinemaApiUrl;

    private final WebClient webClient;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Movie> requestToGetAllMovies() {
        return this.webClient.get()
                .uri(cinemaApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Movie>>() {
                })
                .block();

        /*if (movies != null) {
            movies.forEach(Movie::savePosterImageLocally);
        }*/
    }

    public Movie requestToGetMovieById(Integer id) {
        return this.webClient.get()
                .uri(cinemaApiUrl + "/" + id)
                .retrieve()
                .bodyToMono(Movie.class)
                .block();
    }
}
