package ru.rt.library.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.library.domain.Book;
import ru.rt.library.services.ImageLocalHandleService;

import java.util.List;

/**
 * Обработчик запросов к ресурс-серверу, реализующий {@link RestRequestHandler}
 * <p>
 *
 * @author Alexey Baidin
 */
@Component
public class RestRequestHandlerImpl implements RestRequestHandler{

    @Value("${resource-server.api.url}")
    private String libraryApiUrl;

    private final WebClient webClient;

    private final ImageLocalHandleService imageLocalHandleService;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient, ImageLocalHandleService imageLocalHandleService) {
        this.webClient = webClient;
        this.imageLocalHandleService = imageLocalHandleService;
    }

    private List<Book> getAllBooks() {
        return this.webClient.get()
                .uri(libraryApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {
                })
                .block();
    }

    public List<Book> requestToGetAllBooks() {
        return this.getAllBooks();
    }

    @Override
    public List<Book> requestToGetAllBooksAndSaveLocally() {
        List<Book> books = this.getAllBooks();

        books.forEach(imageLocalHandleService::saveCoverImageLocally);

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
