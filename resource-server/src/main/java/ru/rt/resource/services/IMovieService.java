package ru.rt.resource.services;

import ru.rt.resource.domain.Movie;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, предоставляющий методы для работы с сущностями Movie и соответствующим Jpa-репозиторием.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface IMovieService {

    /**
     * Метод получения всех фильмов.
     * @return список сущностей-фильмов
     */
    List<Movie> getAllMovies();

    /**
     * Метод получения фильма по заданному id.
     * @param id уникальный идентификатор фильма
     * @return сущность {@link Movie} or null
     */
    Movie getMovieById(Long id);

    /**
     * Метод получения списка пар идентификаторов и названий файлов изображений фильмов.
     * @return список объектов Map.Entry (первый объект типа Long, второй - типа String)
     */
    List<Map.Entry<Long, String>> getAllMoviesIdsAndFilenames();

    /**
     * Метод установки массива байтов (изображения постера фильма) в соответствующий атрибут таблицы movies в БД.
     * @param posterImage массив байтов изображения
     * @param id уникальный идентификатор фильма
     */
    void setByteArrayToImageOfMovieById(byte[] posterImage, Long id);
}
