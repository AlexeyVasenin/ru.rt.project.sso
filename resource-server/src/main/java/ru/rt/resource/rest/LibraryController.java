package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Book;
import ru.rt.resource.services.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/library")
public class LibraryController {
    private final BookService bookService;

    @Autowired
    public LibraryController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book bookById = bookService.getBookById(id);
        return bookById == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(bookById, HttpStatus.OK);
    }

    @GetMapping(value = "/preload/filenames")
    public List<String> getAllBooksFilenames() {
        return bookService.getAllBooksFilenames();
    }
}
