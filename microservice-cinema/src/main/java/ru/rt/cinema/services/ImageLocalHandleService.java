package ru.rt.cinema.services;

import ru.rt.cinema.domain.Movie;

/**
 * Интерфейс, предоставляющий методы работы с локальными изображениями постеров фильмов.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface ImageLocalHandleService {

    /**
     * Метод локального сохранения изображения в формате jpg в директорию resources.static.images.
     * <p>
     *
     * @param movie POJO-объект Movie
     */
    public void savePosterImageLocally(Movie movie);

    /**
     * Метод получения пути до локального изображения.
     * <p>
     *
     * @param movie POJO-объект Movie
     * @return src - путь до локального изображения
     */
    public String getPosterImageSrc(Movie movie);
}
