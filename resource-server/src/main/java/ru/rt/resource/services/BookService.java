package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Book;
import ru.rt.resource.repos.IBookRepo;

import java.util.List;

@Service
public class BookService {
    private final IBookRepo bookRepo;

    @Autowired
    public BookService(IBookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    public List<String> getAllBooksFilenames() {
        return bookRepo.getAllBooksFilenames();
    }
}
