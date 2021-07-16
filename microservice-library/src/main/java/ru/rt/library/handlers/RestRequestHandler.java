package ru.rt.library.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.library.domain.Book;

import java.util.List;

@Component
public class RestRequestHandler {
    @Value("${resource-server.api.url}")
    private String libraryApiUrl;

    @Autowired
    private WebClient webClient;

    public List<Book> requestToGetAllBooks() {
        List<Book> books = this.webClient.get()
                .uri(libraryApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {
                })
                .block();
        // для локального сохранения изображений
//        if (books != null) {
//            books.forEach(Book::saveCoverImageLocally);
//        }

        return books;
    }

    public Book requestToGetBookById(Integer id) {
        return this.webClient.get()
                .uri(libraryApiUrl + "/" + id)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
}
