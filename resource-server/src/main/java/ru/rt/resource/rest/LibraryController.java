package ru.rt.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Book;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/library")
public class LibraryController {

    private final List<Book> books = new ArrayList<>() {{
        add(new Book(1L, "Евгений Онегин", "А.С. Пушкин"));
        add(new Book(2L, "Идиот", "Ф.М. Достоевский"));
    }};

    @GetMapping
    public List<Book> findAll() {
        return books;
    }
}
