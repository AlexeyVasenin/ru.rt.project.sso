package ru.rt.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Song;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/music")
public class MusicController {

    private final List<Song> songs = new ArrayList<>() {{
        add(new Song(1L, "Bohemian Rhapsody", "Queen"));
        add(new Song(2L, "Never Gonna Give You Up", "Rick Astley"));
    }};

    @GetMapping
    public List<Song> findAll() {
        return songs;
    }
}
