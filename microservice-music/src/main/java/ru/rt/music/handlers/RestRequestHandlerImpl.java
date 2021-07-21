package ru.rt.music.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.music.domain.Song;
import ru.rt.music.services.ImageLocalHandleService;

import java.util.List;

/**
 * Обработчик запросов к ресурс-серверу, реализующий {@link RestRequestHandler}
 * <p>
 *
 * @author Alexey Baidin
 */
@Component
public class RestRequestHandlerImpl implements RestRequestHandler {

    @Value("${resource-server.api.url}")
    private String musicApiUrl;

    private final WebClient webClient;

    private final ImageLocalHandleService imageLocalHandleService;

    @Autowired
    public RestRequestHandlerImpl(WebClient webClient, ImageLocalHandleService imageLocalHandleService) {
        this.webClient = webClient;
        this.imageLocalHandleService = imageLocalHandleService;
    }

    private List<Song> getAllSongs() {
        return this.webClient.get()
                .uri(musicApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Song>>() {
                })
                .block();
    }

    public List<Song> requestToGetAllSongs() {
        return this.getAllSongs();
    }

    @Override
    public List<Song> requestToGetAllSongsAndSaveLocally() {
        List<Song> songs = this.getAllSongs();

        songs.forEach(imageLocalHandleService::saveCoverImageLocally);

        return songs;
    }
}
