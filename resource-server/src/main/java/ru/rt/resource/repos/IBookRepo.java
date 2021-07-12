package ru.rt.resource.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rt.resource.domain.Book;

public interface IBookRepo extends JpaRepository<Long, Book>{
}
