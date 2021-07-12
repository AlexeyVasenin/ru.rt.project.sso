package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Song;
import ru.rt.resource.services.SongService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/music")
public class MusicController {
    private final SongService songService;

    @Autowired
    public MusicController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return new ResponseEntity<>(songService.getAllSongs(), HttpStatus.OK);
    }

    @GetMapping(value = "/preload/filenames")
    public List<String> getAllSongsFilenames() {
        return songService.getAllSongsFilenames();
    }
}
