package ru.rt.resource.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TypesConverter {
    /**
     * Метод, который позволяет траснформировать локальное .jpg-изображение в массив байтов.
     * @param directory название поддиректории в директории resources/static/images
     * @param filename навзание файла
     * @return массив байтов
     */
    public byte[] convertImageToBytesArray(String directory, String filename) {
        byte[] result = null;
        try {
            String path = String.format("resource-server/src/main/resources/static/images/%s/%s.jpg", directory, filename);
            File f = new File(path);
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

    /**
     * Метод, который позволяяет трансформировать коллекцию массивов объектов в коллекцию пар типов Long и String.
     * @param arrayList коллекция Object-массивов
     * @return коллекция Map.Entry
     */
    public List<Map.Entry<Long, String>> convertObjectArrayToMapList(List<Object[]> arrayList) {
        List<Map.Entry<Long, String>> result = new ArrayList<>();
        arrayList.forEach(x -> {
            // x[0] - Long, x[1] - String
            result.add(new AbstractMap.SimpleEntry<>((Long) x[0], (String) x[1]));
        });
        return result;
    }
}
