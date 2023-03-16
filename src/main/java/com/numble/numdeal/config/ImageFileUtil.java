package com.numble.numdeal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class ImageFileUtil {
    private static String fileDirectory;

    @Value("${numdeal.timedeal.image.src}")
    public void setFileDirectory(String directory) {
        fileDirectory = directory;
    }

    public static String encode(String imageFileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileDirectory + imageFileName);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        inputStream.transferTo(byteArrayOutputStream);

        inputStream.close();

        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
