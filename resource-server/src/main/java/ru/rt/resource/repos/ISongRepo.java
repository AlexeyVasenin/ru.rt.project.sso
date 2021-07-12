package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rt.resource.domain.Song;

import java.util.List;

public interface ISongRepo extends JpaRepository<Song, Long> {
    @Query(value = "SELECT s.filename from Song s")
    List<String> getAllSongsFilenames();
}
