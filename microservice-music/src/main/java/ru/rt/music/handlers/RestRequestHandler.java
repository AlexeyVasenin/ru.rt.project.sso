package ru.rt.music.handlers;

import ru.rt.music.domain.Song;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы для работы с...
 */
//todo A. Baidin описание класса
public interface RestRequestHandler {

    List<Song> requestToGetAllSongs();
}
