package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Movie;
import ru.rt.resource.repos.IMovieRepo;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

//todo A. Baidin описание класса
@Service
public class MovieService {

    private final IMovieRepo movieRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public MovieService(IMovieRepo movieRepo, TypesConverter typesConverter) {
        this.movieRepo = movieRepo;
        this.typesConverter = typesConverter;
    }

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepo.findById(id).orElse(null);
    }

    public List<Map.Entry<Long, String>> getAllMoviesIdsAndFilenames() {
        List<Object[]> allMoviesIdsAndFilenames = movieRepo.getAllMoviesIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allMoviesIdsAndFilenames);
    }

    public void setByteArrayToImageOfMovieById(byte[] posterImage, Long id) {
        movieRepo.setByteArrayToImageOfMovieById(posterImage, id);
    }
}
