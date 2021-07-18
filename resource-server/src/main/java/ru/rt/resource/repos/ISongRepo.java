package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.rt.resource.domain.Song;

import java.util.List;

//todo A. Baidin описание класса, методов
public interface ISongRepo extends JpaRepository<Song, Long> {

    @Query(value = "SELECT s.id, s.filename from Song s")
    List<Object[]> getAllSongsIdsAndFilenames();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Song s SET s.albumCover = :albumCover WHERE s.id = :id")
    void setByteArrayToImageOfSongCoverById(@Param("albumCover") byte[] albumCover, @Param("id") Long id);
}
