package ru.rt.resource.services;

import ru.rt.resource.domain.Song;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, предоставляющий методы для работы с сущностями Song и соответствующим Jpa-репозиторием.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface ISongService {

    /**
     * Метод получения всех песен.
     * @return список сущностей-песен
     */
    List<Song> getAllSongs();

    /**
     * Метод получения списка пар идентификаторов и названий файлов изображений обложек песен.
     * @return список объектов Map.Entry (первый объект типа Long, второй - типа String)
     */
    List<Map.Entry<Long, String>> getAllSongsIdsAndFilenames();

    /**
     * Метод установки массива байтов (изображения обложки песни) в соответствующий атрибут таблицы songs в БД.
     * @param albumCover массив байтов изображения
     * @param id уникальный идентификатор песни
     */
    void setByteArrayToImageOfSongCoverById(byte[] albumCover, Long id);
}
