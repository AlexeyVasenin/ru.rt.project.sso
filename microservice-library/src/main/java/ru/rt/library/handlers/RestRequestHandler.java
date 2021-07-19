package ru.rt.library.handlers;

import ru.rt.library.domain.Book;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы обработки запросов к ресурс-серверу.
 * <p>
 *
 * @author Alexey Baidin
 */
public interface RestRequestHandler {

    /**
     * Запрос к серверу ресурсов на получение всех книг.
     * <p>
     *
     * @return список POJO-объектов Book
     */
    List<Book> requestToGetAllBooks();

    /**
     * Запрос к серверу ресурсов на получение всех книг и сохранение изображений обложек книг локально.
     * <p>
     *
     * @return список POJO-объектов Book
     */
    @Deprecated
    List<Book> requestToGetAllBooksAndSaveLocally();

    /**
     * Запрос к серверу ресурсов на получение книги по заданному id.
     * <p>
     *
     * @param id уникальный идентификатор сущности Book
     * @return POJO-объект Book с заданным id or null
     */
    Book requestToGetBookById(Integer id);
}
