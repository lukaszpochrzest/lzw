package org.lzw;

import org.exception.InvalidParamsException;
import org.gen.GaussianGenerator;
import org.gen.Generator;
import org.gen.LaplaceGenerator;
import org.gen.UniformGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lzw.Logger.log;
import static org.lzw.Logger.testLog;
import static org.lzw.ProgramParams.*;


/**
 * Created by lukasz on 04.03.16.
 */
public class Console {

    public static void main (String[] args) {
        parse(args);

        //  help ?

        if(isHelp()) {
            System.out.println(paramsDescription());
            System.exit(0);
        }

        try {
            //  encode/decode/generate ?

            boolean isEncode = isEncode();
            boolean isDecode = isDecode();
            boolean isGenerate = isGenerate();

            if( isGenerate ) {

                generate();

            } else if(isEncode && !isDecode) { //  encode

                encode();

            } else if(isDecode && !isEncode) {  //  decode

                decode();

            } else {

                throw new InvalidParamsException("Choose between encoding, decoding or generating...");

            }

        } catch (InvalidParamsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }


    }

    private static void generate() throws InvalidParamsException{

        //  output file
        String outputFileName = requiresOutputFileName();

        //  distribution
        Distribution distribution = requiresDistribution();

        //  create generator
        Generator generator = null;
        if( distribution == Distribution.Uniform )
            generator = new UniformGenerator();
        else if( distribution == Distribution.Gauss )
            generator = new GaussianGenerator();
        else if( distribution == Distribution.Laplace ) {
            generator = new LaplaceGenerator(/*paramMu, paramBeta*/);
        }
        else {
            throw new RuntimeException();
        }

        try
        {
            //  generate
            BufferedImage generatedData = generator.GenerateImage(500, 500);

            //  write to output file
            File outputFile = new File( outputFileName );
            ImageIO.write(generatedData, "bmp", outputFile);

            log("Writing generated data to a file: " + System.getProperty("user.dir") + "/" + outputFileName);
        }
        catch ( IOException e )
        {
            throw new InvalidParamsException("Error writing to a file: " + outputFileName);
        }
    }

    private static void encode() throws InvalidParamsException {

        //  get output file name
        String outputFileName = requiresOutputFileName();

        //  input file
        byte[] inputData = requiresInputFile();

        //  compute entropy
        double inputEntropy = EntropyUtils.computeEntropy(inputData);

        //  start timer
        long startTimeMs = System.currentTimeMillis();

        //  encode data
        byte[] encodedData  = LZW.encode(inputData);

        //  end timer
        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        //  log compression percentage
        testLog(getInputFileName() + " | " + (1 - ((float) encodedData.length / inputData.length)) * 100 + "%");

        //  compute avg bit length
        double avgBitLength = (double)inputData.length * 8 / encodedData.length;

        //  log entropy and avg bit length comparison
        testLog("Entropy vs avg bit length: " + String.format( "%.2f", inputEntropy )  + " " + String.format( "%.2f", avgBitLength ));

        //  write encoded data to output file
        writeOutput(outputFileName, encodedData);

        //  log time
        log("Operation lasted " + (float) elapsedMs / 1000 + "s");

    }

    private static void decode() throws InvalidParamsException {

        //  get output file name
        String outputFileName = requiresOutputFileName();

        //  input file
        byte[] inputData = requiresInputFile();

        //  start timer
        long startTimeMs = System.currentTimeMillis();

        //  decode data
        byte[] decodedData = LZW.decode(inputData);    //TODO catch invalid input file format (is this file a compressed one?)

        //  end timer
        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        //  write decoded data to output file
        if(decodedData != null) {
            writeOutput(outputFileName, decodedData);
        } else {
            System.out.println("No data to write"); //TODO do not allow decodedData to be null here
            System.exit(1);
        }

        //  log time
        log("Operation lasted " + (float) elapsedMs / 1000 + "s");

    }

    private static byte[] requiresInputFile() throws InvalidParamsException {
        String inputFileName = getInputFileName();
        try {
            if(inputFileName != null) {
                return IOUtils.read(inputFileName);
            } else {
                throw new InvalidParamsException("Input file name needs to be specified");
            }
        } catch (IOException e) {
            throw new InvalidParamsException("Error reading file: " + inputFileName);
        }
    }

    private static String requiresOutputFileName() throws InvalidParamsException {
        String outputFileName = getOutputFileName();
        if(outputFileName != null) {
            return outputFileName;
        } else {
            throw new InvalidParamsException("Output file needs to be defined");
        }
    }

    private static Distribution requiresDistribution() throws InvalidParamsException {
        Distribution distribution = getDistribution();
        if(distribution == Distribution.Unknown || distribution == null) {
            throw new InvalidParamsException("Distribution needs to be specified");
        }
        return distribution;
    }

    private static void writeOutput(String outputFileName, byte[] output) throws InvalidParamsException {
        try {
            IOUtils.write(outputFileName, output);
        } catch (IOException e) {
            throw new InvalidParamsException("Error writing to a file: " + outputFileName);
        }
    }

}
