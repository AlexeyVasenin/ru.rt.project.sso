package ru.rt.library.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * POJO объект для книги.
 * <p>
 *
 * @author Alexey Baidin
 */
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
    public String imageSrc;
}
