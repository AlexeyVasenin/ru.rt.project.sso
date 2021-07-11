package ru.rt.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        add(new Movie(1, "a_beautiful_mind", "Игры разума", 2001, 8.6, convertMoviePosterImageToBytesArray("a_beautiful_mind"), "k6HI8L3YFb8",
                "От всемирной известности до греховных глубин — все это познал на своей шкуре Джон Форбс Нэш-младший. Математический гений, он на заре своей карьеры сделал титаническую работу в области теории игр, которая перевернула этот раздел математики и практически принесла ему международную известность.\n" +
                        "\n" +
                        "Однако буквально в то же время заносчивый и пользующийся успехом у женщин Нэш получает удар судьбы, который переворачивает уже его собственную жизнь."));
        add(new Movie(2, "inception","Начало", 2010, 8.7, convertMoviePosterImageToBytesArray("inception"), "1WF9y_zZ0N8",
                "Кобб – талантливый вор, лучший из лучших в опасном искусстве извлечения: он крадет ценные секреты из глубин подсознания во время сна, когда человеческий разум наиболее уязвим. Редкие способности Кобба сделали его ценным игроком в привычном к предательству мире промышленного шпионажа, но они же превратили его в извечного беглеца и лишили всего, что он когда-либо любил.\n" +
                        "\n" +
                        "И вот у Кобба появляется шанс исправить ошибки. Его последнее дело может вернуть все назад, но для этого ему нужно совершить невозможное – инициацию. Вместо идеальной кражи Кобб и его команда спецов должны будут провернуть обратное. Теперь их задача – не украсть идею, а внедрить ее. Если у них получится, это и станет идеальным преступлением."));
        add(new Movie(3, "joker","Джокер", 2019, 8., convertMoviePosterImageToBytesArray("joker"), "VCMaJLwChfs",
                "Готэм, начало 1980-х годов. Комик Артур Флек живет с больной матерью, которая с детства учит его «ходить с улыбкой». Пытаясь нести в мир хорошее и дарить людям радость, Артур сталкивается с человеческой жестокостью и постепенно приходит к выводу, что этот мир получит от него не добрую улыбку, а ухмылку злодея Джокера."));
        add(new Movie(4, "the_lord_of_the_rings_III","Властелин колец: Возвращение короля", 2003, 8.6, convertMoviePosterImageToBytesArray("the_lord_of_the_rings_III"), "lxAeV1-KpSA",
                "Повелитель сил тьмы Саурон направляет свою бесчисленную армию под стены Минас-Тирита, крепости Последней Надежды. Он предвкушает близкую победу, но именно это мешает ему заметить две крохотные фигурки — хоббитов, приближающихся к Роковой Горе, где им предстоит уничтожить Кольцо Всевластья."));
        add(new Movie(5, "the_silence_of_the_lambs","Молчание ягнят", 1991, 8.3, convertMoviePosterImageToBytesArray("the_silence_of_the_lambs"), "9JTNhAOVosk",
                "Психопат похищает и убивает молодых женщин по всему Среднему Западу Америки. ФБР, уверенное в том, что все преступления совершены одним и тем же человеком, поручает агенту Клариссе Старлинг встретиться с заключенным-маньяком, который мог бы объяснить следствию психологические мотивы серийного убийцы и тем самым вывести на его след.\n" +
                        "\n" +
                        "Заключенный, доктор психиатрии Ганнибал Лектер, отбывает наказание за убийства и каннибализм. Он согласен помочь Клариссе лишь в том случае, если она попотчует его больное воображение подробностями своей сложной личной жизни. Такие двусмысленные отношения не только порождают в душе Клариссы внутренний конфликт, но и сталкивают ее лицом к лицу с помешанным до гениальности убийцей."));
        add(new Movie(6, "the_social_network","Социальная сеть", 2010, 7.7, convertMoviePosterImageToBytesArray("the_social_network"), "54izAMhnj0c",
                "В фильме рассказывается история создания одной из самых популярных в Интернете социальных сетей - Facebook. Оглушительный успех этой сети среди пользователей по всему миру навсегда изменил жизнь студентов-однокурсников гарвардского университета, которые основали ее в 2004 году и за несколько лет стали самыми молодыми мультимиллионерами в США."));
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

    @GetMapping(value = "/{id}")
    public Movie getMovieById(@PathVariable Integer id) {
        return movies.stream().filter(x -> x.id.equals(id)).findFirst().orElse(null);
    }
}
