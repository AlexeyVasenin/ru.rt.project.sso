package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rt.resource.domain.Book;

import java.util.List;

public interface IBookRepo extends JpaRepository<Book, Long>{
    @Query(value = "SELECT b.filename from Book b")
    List<String> getAllBooksFilenames();
}
