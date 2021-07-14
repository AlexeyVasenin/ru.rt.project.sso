package ru.rt.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rt.library.handlers.RestRequestHandler;

import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private RestRequestHandler restRequestHandler;

    @GetMapping("/{id}")
    public String getBookPage(@PathVariable Integer id, Model model, Principal principal) {
        model.addAttribute("principal", principal);
        model.addAttribute("book", restRequestHandler.requestToGetBookById(id));
        return "book_page";
    }
}
