package ru.rt.resource.services;

import ru.rt.resource.domain.Book;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс, предоставляющий методы для работы с сущностями Book и соответствующим Jpa-репозиторием.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface IBookService {

    /**
     * Метод получения всех книг.
     * @return список сущностей-книг
     */
    List<Book> getAllBooks();

    /**
     * Метод получения книги по заданному id.
     * @param id уникальный идентификатор книги
     * @return сущность {@link Book} or null
     */
    Book getBookById(Long id);

    /**
     * Метод получения списка пар идентификаторов и названий файлов изображений книг.
     * @return список объектов Map.Entry (первый объект типа Long, второй - типа String)
     */
    List<Map.Entry<Long, String>> getAllBooksIdsAndFilenames();

    /**
     * Метод установки массива байтов (изображения обложки книги) в соответствующий атрибут таблицы books в БД.
     * @param coverImage массив байтов изображения
     * @param id уникальный идентификатор книги
     */
    void setByteArrayToImageOfBookCoverById(byte[] coverImage, Long id);
}
