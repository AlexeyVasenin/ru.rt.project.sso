package ru.rt.cinema.services;

import org.springframework.stereotype.Service;
import ru.rt.cinema.domain.Movie;
import ru.rt.cinema.handlers.RestRequestHandler;

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

    private final String SERVICE_NAME = "microservice_cinema";

    @Override
    public void savePosterImageLocally(Movie movie) {
        try {
            Path path = Paths.get(SERVICE_NAME + "/src/main/resources/static/images/" + movie.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(movie.posterImage);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPosterImageSrc(Movie movie) {
        return String.format("/static/images/%s.jpg", movie.filename);
    }
}
