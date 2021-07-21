package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

/**
 * Rest Controller для обработки внешних запросов к БД (например, для прокидывания локальных изображений сущностей в базу).
 * <p>
 *
 * @author Alexey Baidin
 */
@RestController
@RequestMapping(value = "/api/database")
public class DatabaseController {

    @Value("${resource-server.database.secret}")
    public String databaseSecret;

    private final CinemaController cinemaController;

    private final LibraryController libraryController;

    private final MusicController musicController;

    private final TypesConverter typesConverter;

    @Autowired
    public DatabaseController(CinemaController cinemaController, LibraryController libraryController,
                              MusicController musicController, TypesConverter typesConverter) {
        this.cinemaController = cinemaController;
        this.libraryController = libraryController;
        this.musicController = musicController;
        this.typesConverter = typesConverter;
    }

    @GetMapping
    public ResponseEntity<Void> updateDatabaseTablesWithLocalImages(@RequestParam String secret) {
        if (databaseSecret.equals(secret)) {
            List<Map.Entry<Long, String>> moviesIdsAndFilenames = cinemaController.getAllMoviesIdsAndFilenames();
            for (Map.Entry<Long, String> entry : moviesIdsAndFilenames) {
                byte[] posterArray = typesConverter.convertImageToBytesArray("movies", entry.getValue());
                cinemaController.setByteArrayToImageOfMovieById(posterArray, entry.getKey());
            }

            List<Map.Entry<Long, String>> booksIdsAndFilenames = libraryController.getAllBooksIdsAndFilenames();
            for (Map.Entry<Long, String> entry : booksIdsAndFilenames) {
                byte[] coverImage = typesConverter.convertImageToBytesArray("books", entry.getValue());
                libraryController.setByteArrayToImageOfBookCoverById(coverImage, entry.getKey());
            }

            List<Map.Entry<Long, String>> songsIdsAndFilenames = musicController.getAllSongsIdsAndFilenames();
            for (Map.Entry<Long, String> entry : songsIdsAndFilenames) {
                byte[] albumCover = typesConverter.convertImageToBytesArray("album_covers", entry.getValue());
                musicController.setByteArrayToImageOfSongCoverById(albumCover, entry.getKey());
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
