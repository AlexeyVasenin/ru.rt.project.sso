package ru.rt.music.handlers;

import ru.rt.music.domain.Song;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы обработки запросов к ресурс-серверу.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface RestRequestHandler {

    /**
     * Запрос к серверу ресурсов на получение всех песен.
     * <p>
     *
     * @return список POJO-объектов Song
     */
    List<Song> requestToGetAllSongs();

    /**
     * Запрос к серверу ресурсов на получение всех песен и сохранение изображений обложек альбомов локально.
     * <p>
     *
     * @return список POJO-объектов Song
     */
    @Deprecated
    List<Song> requestToGetAllSongsAndSaveLocally();
}
