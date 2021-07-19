package ru.rt.music.domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * POJO объект для песни.
 * <p>
 *
 * @author Alexey Baidin
 */
public class Song {

    public Long id;
    public String title;
    public String musicianName;
    public Double rating;
    public String duration;
    public byte[] albumCover;
    public String filename;
    public String imageSrc;
}
