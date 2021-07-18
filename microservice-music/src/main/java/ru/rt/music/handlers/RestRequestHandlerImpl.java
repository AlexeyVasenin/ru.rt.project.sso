package ru.rt.music.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.music.domain.Song;

import java.util.List;

//todo A. Baidin описание класса, методов --> доступ в интерйефс, реализация тут.
// Работаем с представлением, а не реализацией доки также в интерефейс
@Component
public class RestRequestHandlerImpl implements RestRequestHandler {

    @Value("${resource-server.api.url}")
    private String musicApiUrl;

    private final WebClient webClient;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Song> requestToGetAllSongs() {
        return this.webClient.get()
                .uri(musicApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Song>>() {
                })
                .block();

        /*if (songs != null) {
            songs.forEach(Song::saveCoverImageLocally);
        }*/
    }
}
