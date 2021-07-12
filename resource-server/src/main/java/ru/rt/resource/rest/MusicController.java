package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rt.resource.domain.Song;
import ru.rt.resource.services.SongService;

import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "/{id}")
    public ResponseEntity<Void> setByteArrayToImageOfSongCoverById(@RequestParam byte[] albumCover, @PathVariable Long id) {
        songService.setByteArrayToImageOfSongCoverById(albumCover, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/preload/filenames")
    public List<Map.Entry<Long, String>> getAllSongsIdsAndFilenames() {
        return songService.getAllSongsIdsAndFilenames();
    }
}
