package org.lzw;

import static org.lzw.ProgramParams.*;

import ImageGen.GaussianImageGenerator;
import ImageGen.ImageGenerator;
import ImageGen.LaplaceImageGenerator;
import ImageGen.UniformImageGenerator;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

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
        parse(args);

        //  help

        if(isHelp()) {
            System.out.println(paramsDescription());
            return;
        }

        //  output file

        String outputFileName = getOutputFileName();

        if(outputFileName == null) {
            System.out.println("Output file needs to be defined");
            System.exit(1);
        }

        //  input file

        String inputFileName = getInputFileName();
        byte[] inputData = null;

        try {
            if(inputFileName != null) {
                inputData = read(inputFileName);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFileName);
            System.exit(1);
        }

        //  encode/decode

        boolean isEncode = isEncode();
        boolean isDecode = isDecode();
        boolean isGenerate = isGenerate();

        byte[] dataToWriteToFile = null;

        long startTimeMs = System.currentTimeMillis();

        if( isGenerate )
        {
            try
            {
                ImageGenerator generator = null;
                if( getDistribution() == ImageDistribution.Uniform )
                    generator = new UniformImageGenerator();
                else if( getDistribution() == ImageDistribution.Gauss )
                    generator = new GaussianImageGenerator();
                else if( getDistribution() == ImageDistribution.Laplace ) {
                    double paramMu = 0.0;
                    double paramBeta = 0.0;
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("Enter value of mu:");
                    try {
                        paramMu = Double.parseDouble(br.readLine());
                    } catch (NumberFormatException nfe) {
                        System.err.println("Invalid Format!");
                    }
                    System.out.print("Enter value of beta and it must be positive:");
                    try {
                        paramBeta = Double.parseDouble(br.readLine());
                    } catch (NumberFormatException nfe) {
                        System.err.println("Invalid Format!");
                    }
                    generator = new LaplaceImageGenerator(paramMu, paramBeta);
                    }
                else
                {
                    System.out.println( "Error: distribution not set" );
                    System.exit(1);
                }

                BufferedImage image = generator.GenerateImage( 500, 500 );
                File outputfile = new File( outputFileName );
                ImageIO.write(image, "bmp", outputfile);

                System.out.println( "Writing image to a file: " + System.getProperty("user.dir") + "/" + outputFileName);
                System.exit(1);
            }
            catch ( IOException e )
            {
                System.out.println( e.getMessage() );
                System.exit(1);
            }
        }
        else if(isEncode && !isDecode) { //  encode
            //  encode data
            dataToWriteToFile = LZW.encode(inputData);
        } else if(isDecode && !isEncode) {  //  decode
            //  decode data
            try{
                dataToWriteToFile = LZW.decode(inputData);
            } catch(Exception e) {
                System.out.println("Invalid input file (is this file a compressed one?)");
                System.exit(1);
            }
        } else {
            System.out.println("Choose between encoding, decoding or generating...");
            System.exit(1);
        }

        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        if(isVerbose()) {
            System.out.println("Operation lasted " + (float)elapsedMs / 1000 + "s");
        }

        //  write to file

        if(dataToWriteToFile != null) {
            try {
                write(outputFileName, dataToWriteToFile);
            } catch (IOException e) {
                System.out.println("Error writing to a file: " + outputFileName);
                System.exit(1);
            }
        } else {
            System.out.println("No data to write");
            System.exit(1);
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
