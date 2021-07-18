package ru.rt.cinema.handlers;

import ru.rt.cinema.domain.Movie;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы для работы с...
 */
//todo A. Baidin описание класса
public interface RestRequestHandler {

    /**
     *
     * @return
     */
    List<Movie> requestToGetAllMovies();

    Movie requestToGetMovieById(Integer id);
}
