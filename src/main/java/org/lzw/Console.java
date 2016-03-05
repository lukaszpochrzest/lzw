package org.lzw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lukasz on 04.03.16.
 */
public class Console {

    public static void main (String[] args) {
        ProgramParams pp = new ProgramParams(args);

        //  help

        if(pp.isHelp()) {
            System.out.println(ProgramParams.paramsDescription());
            return;
        }

        //  output file

        String outputFileName = pp.getOutputFileName();

        if(outputFileName == null) {
            System.out.println("Output file needs to be defined");
            return;
        }

        //  input file

        String inputFileName = pp.getInputFileName();
        byte[] inputData = null;

        try {
            if(inputFileName != null) {
                inputData = read(inputFileName);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFileName);
            return;
        }

        //  encode/decode

        boolean isEncode = pp.isEncode();
        boolean isDecode = pp.isDecode();

        byte[] dataToWriteToFile = null;

        long startTimeMs = System.currentTimeMillis();

        if(isEncode && !isDecode) { //  encode
            //  encode data
            dataToWriteToFile = LZW.encode(inputData);
        } else if(isDecode && !isEncode) {  //  decode
            //  decode data
            dataToWriteToFile = LZW.decode(inputData);
        } else {
            System.out.println("Choose between encoding and decoding...");
            return;
        }

        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        if(pp.isVerbose()) {
            System.out.println("Operation lasted " + (float)elapsedMs / 1000 + "s");
        }

        //  write to file

        if(dataToWriteToFile != null) {
            try {
                write(outputFileName, dataToWriteToFile);
            } catch (IOException e) {
                System.out.println("Error writing to a file: " + outputFileName);
            }
        } else {
            System.out.println("No data for write");
        }

    }

    public static void write(String fileName, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(data);
        fos.close();
    }

    public static byte[] read(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllBytes(path);
    }

}
