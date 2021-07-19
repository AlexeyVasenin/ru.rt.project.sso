package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Book;
import ru.rt.resource.repos.IBookRepo;
import ru.rt.resource.rest.LibraryController;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с сущностями Book, связывающий между собой контроллер {@link LibraryController} и репозиторий
 * {@link IBookRepo}, а также реализующий {@link IBookService}.
 * <p>
 *
 * @author Alexey Baidin
 */
@Service
public class BookServiceImpl implements IBookService {

    private final IBookRepo bookRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public BookServiceImpl(IBookRepo bookRepo, TypesConverter typesConverter) {
        this.bookRepo = bookRepo;
        this.typesConverter = typesConverter;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepo.findById(id).orElse(null);
    }

    @Override
    public List<Map.Entry<Long, String>> getAllBooksIdsAndFilenames() {
        List<Object[]> allBooksIdsAndFilenames = bookRepo.getAllBooksIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allBooksIdsAndFilenames);
    }

    @Override
    public void setByteArrayToImageOfBookCoverById(byte[] coverImage, Long id) {
        bookRepo.setByteArrayToImageOfSongCoverById(coverImage, id);
    }
}
