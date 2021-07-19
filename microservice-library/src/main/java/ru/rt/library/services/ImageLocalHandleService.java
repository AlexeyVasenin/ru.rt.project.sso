package ru.rt.library.services;

import ru.rt.library.domain.Book;

/**
 * Интерфейс, предоставляющий методы работы с локальными изображениями обложек книг.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface ImageLocalHandleService {

    /**
     * Метод локального сохранения изображения в формате jpg в директорию resources.static.images.
     * <p>
     *
     * @param book POJO-объект Book
     */
    void saveCoverImageLocally(Book book);

    /**
     * Метод получения пути до локального изображения.
     * <p>
     *
     * @param book POJO-объект Book
     * @return src - путь до локального изображения
     */
    String getCoverImageSrc(Book book);
}
