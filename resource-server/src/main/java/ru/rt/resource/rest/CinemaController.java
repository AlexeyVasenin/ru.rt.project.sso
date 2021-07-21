package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rt.resource.domain.Movie;
import ru.rt.resource.services.IMovieService;
import ru.rt.resource.services.MovieServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Rest Controller для обработки запросов от микросервиса с фильмами.
 * <p>
 *
 * @author Alexey Baidin
 */
@RestController
@RequestMapping(value = "/api/cinema")
public class CinemaController {
    private final IMovieService movieService;

    @Autowired
    public CinemaController(IMovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> findAll() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movieById = movieService.getMovieById(id);
        return movieById == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(movieById, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Void> setByteArrayToImageOfMovieById(@RequestParam byte[] posterImage, @PathVariable Long id) {
        movieService.setByteArrayToImageOfMovieById(posterImage, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/preload/filenames")
    public List<Map.Entry<Long, String>> getAllMoviesIdsAndFilenames() {
        return movieService.getAllMoviesIdsAndFilenames();
    }
}
