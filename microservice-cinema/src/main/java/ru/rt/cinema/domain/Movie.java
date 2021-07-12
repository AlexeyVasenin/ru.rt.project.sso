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
    public Path posterFilePath;
    public String youtubeCode;
    public String description;

    public void savePosterImageLocally() {
        try {
            this.posterFilePath = Paths.get("microservice-cinema/src/main/resources/static/images/" + this.filename + ".jpg");
            if (Files.notExists(this.posterFilePath)) {
                ByteArrayInputStream is = new ByteArrayInputStream(this.posterImage);
                BufferedImage image = ImageIO.read(is);
                ImageIO.write(image, "jpg", this.posterFilePath.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPosterImageSrc() {
        String s = this.posterFilePath.toString();
        String fromStaticFolder = s.substring(s.indexOf("\\static"));
        return fromStaticFolder.replaceAll("\\\\", "/");
    }
}
