package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.rt.resource.domain.Book;

import java.util.List;

public interface IBookRepo extends JpaRepository<Book, Long>{
    @Query(value = "SELECT b.id, b.filename from Book b")
    List<Object[]> getAllBooksIdsAndFilenames();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Book b SET b.coverImage = :coverImage WHERE b.id = :id")
    void setByteArrayToImageOfSongCoverById(@Param("coverImage") byte[] coverImage, @Param("id") Long id);
}
