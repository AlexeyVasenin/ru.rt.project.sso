package ru.rt.resource.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Component
public class ImageToBytesConverter {
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
}
