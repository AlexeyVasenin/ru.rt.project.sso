package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Movie;
import ru.rt.resource.repos.IMovieRepo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {
    private final IMovieRepo movieRepo;

    @Autowired
    public MovieService(IMovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepo.findById(id).orElse(null);
    }

    public List<Map.Entry<Long, String>> getAllMoviesIdsAndFilenames() {
        List<Object[]> allMoviesIdsAndFilenames = movieRepo.getAllMoviesIdsAndFilenames();
        List<Map.Entry<Long, String>> result = new ArrayList<>();
        allMoviesIdsAndFilenames.forEach(x -> {
            // x[0] - Long, x[1] - String
            result.add(new AbstractMap.SimpleEntry<>((Long) x[0], (String) x[1]));
        });
        return result;
    }

    public void setByteArrayToImageOfMovieById(byte[] posterImage, Long id) {
        movieRepo.setByteArrayToImageOfMovieById(posterImage, id);
    }
}
