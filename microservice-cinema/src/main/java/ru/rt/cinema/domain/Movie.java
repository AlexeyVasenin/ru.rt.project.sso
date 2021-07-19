package ru.rt.cinema.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * POJO объект для фильма.
 * <p>
 *
 * @author Alexey Baidin
 */
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
}
