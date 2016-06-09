package org.lzw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lzw.Logger.log;

/**
 * Created by lukasz on 09.06.16.
 */
public class IOUtils {

    public static void write(String fileName, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(data);
        fos.close();
    }

    public static byte[] read(String fileName) throws IOException {
        log("Reading " + fileName + "...");
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

}
