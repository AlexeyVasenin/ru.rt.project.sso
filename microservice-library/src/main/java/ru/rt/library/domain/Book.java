package ru.rt.library.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Book {
    public Long id;
    public String title;
    public String authorName;
    public Double rating;
    public String filename;
    public byte[] coverImage;
    public String genres;
    public String description;
    public Integer volume;
    public Boolean withSubscriptionOnly;

    public void saveCoverImageLocally() {
        try {
            Path path = Paths.get("microservice-library/src/main/resources/static/images/" + this.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(this.coverImage);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCoverImageSrc() {
        return String.format("/static/images/%s.jpg", this.filename);
    }
}
