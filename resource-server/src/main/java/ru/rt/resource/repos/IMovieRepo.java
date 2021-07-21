package ru.rt.resource.repos;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rt.resource.domain.Movie;

import java.util.List;

/**
 * Jpa-репозиторий для сущностей Movie.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface IMovieRepo extends JpaRepository<Movie, Long> {

    /**
     * Метод, возвращающий список пар уникальных идентификаторов и названий файлов изображений фильмов из БД.
     *
     * @return список Object-массивов (id - типа Long, filename - String)
     */
    @Query(value = "SELECT m.id, m.filename from Movie m")
    List<Object[]> getAllMoviesIdsAndFilenames();

    /**
     * Метод, устанавливающий массив байтов (изображения постера фильма) в атрибут posterImage по заданному id.
     *
     * @param posterImage массив байтов изображения постера фильма
     * @param id уникальный идентификатор фильма
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Movie m SET m.posterImage = :posterImage WHERE m.id = :id")
    void setByteArrayToImageOfMovieById(@Param("posterImage") byte[] posterImage, @Param("id") Long id);
}
