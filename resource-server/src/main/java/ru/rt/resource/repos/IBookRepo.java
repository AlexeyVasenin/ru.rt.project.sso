package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.transaction.annotation.Transactional;
import ru.rt.resource.domain.Book;

import java.util.List;

/**
 * Jpa-репозиторий для сущностей Book.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface IBookRepo extends JpaRepository<Book, Long> {

    /**
     * Метод, возвращающий список пар уникальных идентификаторов и названий файлов изображений книг из БД.
     *
     * @return список Object-массивов (id - типа Long, filename - String)
     */
    @Query(value = "SELECT b.id, b.filename from Book b")
    List<Object[]> getAllBooksIdsAndFilenames();

    /**
     * Метод, устанавливающий массив байтов (изображения книги) в атрибут coverImage по заданному id.
     *
     * @param coverImage массив байтов изображения обложки книги
     * @param id уникальный идентификатор книги
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Book b SET b.coverImage = :coverImage WHERE b.id = :id")
    void setByteArrayToImageOfSongCoverById(@Param("coverImage") byte[] coverImage, @Param("id") Long id);
}
