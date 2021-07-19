package ru.rt.cinema.handlers;

import ru.rt.cinema.domain.Movie;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы обработки запросов к ресурс-серверу.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface RestRequestHandler {

    /**
     * Запрос к серверу ресурсов на получение всех фильмов.
     * <p>
     *
     * @return список POJO-объектов Movie
     */
    List<Movie> requestToGetAllMovies();

    /**
     * Запрос к серверу ресурсов на получение всех фильмов и сохранение изображений постеров локально.
     * <p>
     *
     * @return список POJO-объектов Movie
     */
    @Deprecated
    List<Movie> requestToGetAllMoviesAndSaveLocally();

    /**
     * Запрос к серверу ресурсов на получение фильма по заданному id.
     * <p>
     *
     * @param id уникальный идентификатор сущности Movie
     * @return POJO-объект Movie с заданным id or null
     */
    Movie requestToGetMovieById(Integer id);
}
