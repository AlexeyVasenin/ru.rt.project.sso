package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.rt.resource.domain.Song;

import java.util.List;

/**
 * Jpa-репозиторий для сущностей Song.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface ISongRepo extends JpaRepository<Song, Long> {

    /**
     * Метод, возвращающий список пар уникальных идентификаторов и названий файлов изображений обложек песен из БД.
     *
     * @return список Object-массивов (id - типа Long, filename - String)
     */
    @Query(value = "SELECT s.id, s.filename from Song s")
    List<Object[]> getAllSongsIdsAndFilenames();

    /**
     * Метод, устанавливающий массив байтов (изображения обложки песни) в атрибут albumCover по заданному id.
     *
     * @param albumCover массив байтов изображения обложки песни
     * @param id уникальный идентификатор песни
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Song s SET s.albumCover = :albumCover WHERE s.id = :id")
    void setByteArrayToImageOfSongCoverById(@Param("albumCover") byte[] albumCover, @Param("id") Long id);
}
