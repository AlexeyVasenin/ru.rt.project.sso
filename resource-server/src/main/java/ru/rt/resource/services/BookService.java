package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Book;
import ru.rt.resource.repos.IBookRepo;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

//todo A. Baidin описание класса, опять же представление можно в интерфейс, реализация тут
@Service
public class BookService {

    private final IBookRepo bookRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public BookService(IBookRepo bookRepo, TypesConverter typesConverter) {
        this.bookRepo = bookRepo;
        this.typesConverter = typesConverter;
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    public List<Map.Entry<Long, String>> getAllBooksIdsAndFilenames() {
        List<Object[]> allBooksIdsAndFilenames = bookRepo.getAllBooksIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allBooksIdsAndFilenames);
    }

    public void setByteArrayToImageOfBookCoverById(byte[] coverImage, Long id) {
        bookRepo.setByteArrayToImageOfSongCoverById(coverImage, id);
    }
}
