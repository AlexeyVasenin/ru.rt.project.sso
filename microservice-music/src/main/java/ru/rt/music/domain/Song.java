package ru.rt.music.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Song {
    public Long id;
    public String title;
    public String musicianName;
    public Double rating;
    public String duration;
    public byte[] albumCover;
    public String filename;
    public String imageSrc;

    public void saveCoverImageLocally() {
        try {
            Path path = Paths.get("microservice-music/src/main/resources/static/images/" + this.filename + ".jpg");
            if (Files.notExists(path)) {
                ByteArrayInputStream is = new ByteArrayInputStream(this.albumCover);
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
