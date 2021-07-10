package ru.rt.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rt.resource.domain.Movie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cinema")
public class CinemaController {

    // TODO подключить БД, чтобы не хранить информацию о фильмах в коде
    private final List<Movie> movies = new ArrayList<>() {{
        add(new Movie(1, "a_beautiful_mind", "Игры разума", 2001, 8.6, convertMoviePosterImageToBytesArray("a_beautiful_mind")));
        add(new Movie(2, "inception","Начало", 2010, 8.7, convertMoviePosterImageToBytesArray("inception")));
        add(new Movie(3, "joker","Джокер", 2019, 8., convertMoviePosterImageToBytesArray("joker")));
        add(new Movie(4, "the_lord_of_the_rings_III","Властелин колец: Возвращение короля", 2003, 8.6, convertMoviePosterImageToBytesArray("the_lord_of_the_rings_III")));
        add(new Movie(5, "the_silence_of_the_lambs","Молчание ягнят", 1991, 8.3, convertMoviePosterImageToBytesArray("the_silence_of_the_lambs")));
        add(new Movie(6, "the_social_network","Социальная сеть", 2010, 7.7, convertMoviePosterImageToBytesArray("the_social_network")));
    }};

    private byte[] convertMoviePosterImageToBytesArray(String movie) {
        byte[] result = null;
        try {
            File f = new File("resource-server/src/main/resources/static/images/movies/" + movie + ".jpg");
            BufferedImage bi = ImageIO.read(f);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", os);
            result = os.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping
    public Collection<Movie> findAll() {
        return movies;
    }
}
