package ru.rt.resource.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rt.resource.domain.Movie;
import ru.rt.resource.services.MovieService;

import java.util.List;
import java.util.Map;

//todo A. Baidin описание класса
@RestController
@RequestMapping(value = "/api/cinema")
public class CinemaController {
    private final MovieService movieService;

    @Autowired
    public CinemaController(MovieService movieService) {
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
