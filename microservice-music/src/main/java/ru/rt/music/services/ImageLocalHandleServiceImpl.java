package ru.rt.music.services;

import org.springframework.stereotype.Service;
import ru.rt.music.domain.Song;

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

    private final String SERVICE_NAME = "microservice-music";

    @Override
    public void saveCoverImageLocally(Song song) {
        try {
            Path path = Paths.get(SERVICE_NAME + "/src/main/resources/static/images/" + song.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(song.albumCover);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCoverImageSrc(Song song) {
        return String.format("/static/images/%s.jpg", song.filename);
    }
}
