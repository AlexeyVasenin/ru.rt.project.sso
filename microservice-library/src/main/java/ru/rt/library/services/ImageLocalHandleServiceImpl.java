package ru.rt.library.services;

import org.springframework.stereotype.Service;
import ru.rt.library.domain.Book;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Сервис для работы с локальными изображениями, реализующий {@link ImageLocalHandleService}.
 * <p>
 *
 * @author Alexey Baidin
 */
@Service
public class ImageLocalHandleServiceImpl implements ImageLocalHandleService {

    private final String SERVICE_NAME = "microservice-library";

    @Override
    public void saveCoverImageLocally(Book book) {
        try {
            Path path = Paths.get(SERVICE_NAME + "/src/main/resources/static/images/" + book.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(book.coverImage);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCoverImageSrc(Book book) {
        return String.format("/static/images/%s.jpg", book.filename);
    }
}
