package ru.rt.library.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.library.domain.Book;

import java.util.List;

//todo A. Baidin описание класса, методов --> доступ в интерйефс, реализация тут.
// Работаем с представлением, а не реализацией доки также в интерефейс
@Component
public class RestRequestHandlerImpl implements RestRequestHandler{

    @Value("${resource-server.api.url}")
    private String libraryApiUrl;

    private final WebClient webClient;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Book> requestToGetAllBooks() {
        return this.webClient.get()
                .uri(libraryApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {
                })
                .block();

        /*if (books != null) {
            books.forEach(Book::saveCoverImageLocally);
        }*/
    }

    public Book requestToGetBookById(Integer id) {
        return this.webClient.get()
                .uri(libraryApiUrl + "/" + id)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
}
