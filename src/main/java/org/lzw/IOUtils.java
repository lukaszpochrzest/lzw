package org.lzw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lukasz on 09.06.16.
 */
public class IOUtils {

    public static void write(String fileName, byte[] data) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }

    public static byte[] read(String fileName) throws IOException {
        Logger.log("Reading " + fileName + "...");
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

}
