package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rt.resource.domain.Book;
import ru.rt.resource.services.BookServiceImpl;
import ru.rt.resource.services.IBookService;

import java.util.List;
import java.util.Map;

/**
 * Rest Controller для обработки запросов от микросервиса с книгами.
 * <p>
 *
 * @author Alexey Baidin
 */
@RestController
@RequestMapping(value = "/api/library")
public class LibraryController {
    private final IBookService bookService;

    @Autowired
    public LibraryController(IBookService bookService) {
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

    @PostMapping(value = "/{id}")
    public ResponseEntity<Void> setByteArrayToImageOfBookCoverById(@RequestParam byte[] coverImage, @PathVariable Long id) {
        bookService.setByteArrayToImageOfBookCoverById(coverImage, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/preload/filenames")
    public List<Map.Entry<Long, String>> getAllBooksIdsAndFilenames() {
        return bookService.getAllBooksIdsAndFilenames();
    }
}
