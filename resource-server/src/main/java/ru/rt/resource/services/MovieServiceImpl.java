package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Movie;
import ru.rt.resource.repos.IMovieRepo;
import ru.rt.resource.rest.CinemaController;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с сущностями Movie, связывающий между собой контроллер {@link CinemaController} и репозиторий
 * {@link IMovieRepo}, а также реализующий {@link IMovieService}.
 * <p>
 *
 * @author Alexey Baidin
 */
@Service
public class MovieServiceImpl implements IMovieService {

    private final IMovieRepo movieRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public MovieServiceImpl(IMovieRepo movieRepo, TypesConverter typesConverter) {
        this.movieRepo = movieRepo;
        this.typesConverter = typesConverter;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepo.findById(id).orElse(null);
    }

    @Override
    public List<Map.Entry<Long, String>> getAllMoviesIdsAndFilenames() {
        List<Object[]> allMoviesIdsAndFilenames = movieRepo.getAllMoviesIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allMoviesIdsAndFilenames);
    }

    @Override
    public void setByteArrayToImageOfMovieById(byte[] posterImage, Long id) {
        movieRepo.setByteArrayToImageOfMovieById(posterImage, id);
    }
}
