package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.utils.ImageToBytesConverter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/database")
public class DatabaseController {

    @Value("${resource-server.database.secret}")
    public String databaseSecret;

    private final CinemaController cinemaController;

    private final LibraryController libraryController;

    private final MusicController musicController;

    private final ImageToBytesConverter imageToBytesConverter;

    @Autowired
    public DatabaseController(CinemaController cinemaController, LibraryController libraryController,
                              MusicController musicController, ImageToBytesConverter imageToBytesConverter) {
        this.cinemaController = cinemaController;
        this.libraryController = libraryController;
        this.musicController = musicController;
        this.imageToBytesConverter = imageToBytesConverter;
    }

    @GetMapping
    public ResponseEntity<Void> updateDatabaseTablesWithLocalImages(@RequestParam String secret) {
        if (databaseSecret.equals(secret)) {
            List<Map.Entry<Long, String>> moviesIdsAndFilenames = cinemaController.getAllMoviesIdsAndFilenames();

            for (Map.Entry<Long, String> movie : moviesIdsAndFilenames) {
                byte[] posterArray = imageToBytesConverter.convertImageToBytesArray("movies", movie.getValue());
                cinemaController.setByteArrayToImageOfMovieById(posterArray, movie.getKey());
            }

            // TODO сделать тоже самое с изображениями обложек альбомов песен и книг

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
