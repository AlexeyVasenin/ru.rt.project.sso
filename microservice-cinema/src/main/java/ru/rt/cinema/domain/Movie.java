package ru.rt.cinema.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Movie {
    public Integer id;
    public String filename;
    public String title;
    public Integer creationYear;
    public Double rating;
    public byte[] posterImage;
    public String youtubeCode;
    public String description;
    public Boolean withSubscriptionOnly;
    public String imageSrc;

    public void savePosterImageLocally() {
        try {
            Path path = Paths.get("microservice-cinema/src/main/resources/static/images/" + this.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(this.posterImage);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPosterImageSrc() {
        return String.format("/static/images/%s.jpg", this.filename);
    }
}
