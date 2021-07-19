package ru.rt.music.services;

import ru.rt.music.domain.Song;

/**
 * Интерфейс, предоставляющий методы работы с локальными изображениями обложек альбомов песен.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface ImageLocalHandleService {

    /**
     * Метод локального сохранения изображения в формате jpg в директорию resources.static.images.
     * <p>
     *
     * @param song POJO-объект Song
     */
    void saveCoverImageLocally(Song song);

    /**
     * Метод получения пути до локального изображения.
     * <p>
     *
     * @param song POJO-объект Song
     * @return src - путь до локального изображения
     */
    String getCoverImageSrc(Song song);
}
