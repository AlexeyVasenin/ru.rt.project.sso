package ru.rt.music.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.rt.music.domain.Song;

import java.util.List;

@Component
public class RestRequestHandler {
    @Value("${resource-server.api.url}")
    private String musicApiUrl;

    @Autowired
    private WebClient webClient;

    public List<Song> requestToGetAllSongs() {
        List<Song> songs = this.webClient.get()
                .uri(musicApiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Song>>() {
                })
                .block();

        // для локального сохранения изображений
//        if (songs != null) {
//            songs.forEach(Song::saveCoverImageLocally);
//        }

        return songs;
    }
}
