package ru.rt.library.handlers;

import ru.rt.library.domain.Book;

import java.util.List;

/**
 * Интерфейс, предоставляющий методы для работы с...
 */
//todo A. Baidin описание класса
public interface RestRequestHandler {

    List<Book> requestToGetAllBooks();

    Book requestToGetBookById(Integer id);
}
