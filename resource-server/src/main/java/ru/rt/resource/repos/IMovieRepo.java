package ru.rt.resource.repos;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rt.resource.domain.Movie;

import java.util.List;

public interface IMovieRepo extends JpaRepository<Movie, Long> {
    @Query(value = "SELECT m.id, m.filename from Movie m")
    List<Object[]> getAllMoviesIdsAndFilenames();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Movie m SET m.posterImage = :posterImage WHERE m.id = :id")
    void setByteArrayToImageOfMovieById(@Param("posterImage") byte[] posterImage, @Param("id") Long id);
}
