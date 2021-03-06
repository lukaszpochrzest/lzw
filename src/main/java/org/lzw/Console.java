package org.lzw;

import org.lzw.algorithm.LZWDecoder;
import org.lzw.algorithm.LZWEncoder;
import org.lzw.exception.InvalidInputEncodedDataFileException;
import org.lzw.exception.InvalidParamsException;
import org.lzw.generator.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by lukasz on 04.03.16.
 */
public class Console {

    public static void main (String[] args) {
        ProgramParams.parse(args);

        //  help ?

        if(ProgramParams.isHelp()) {
            System.out.println(ProgramParams.paramsDescription());
            System.exit(0);
        }

        try {
            //  encode/decode/generate ?

            boolean isEncode = ProgramParams.isEncode();
            boolean isDecode = ProgramParams.isDecode();
            boolean isGenerate = ProgramParams.isGenerate();

            if( isGenerate ) {

                generate();

            } else if(isEncode && !isDecode) { //  encode

                encode();

            } else if(isDecode && !isEncode) {  //  decode

                decode();

            } else {

                throw new InvalidParamsException("Choose between encoding, decoding or generating...");

            }

        } catch (InvalidParamsException | InvalidInputEncodedDataFileException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    private static void generate() throws InvalidParamsException{

        //  output file
        String outputFileName = requiresOutputFileName();

        //  distribution
        ProgramParams.Distribution distribution = requiresDistribution();

        //  create generator
        Generator generator = null;
        if( distribution == ProgramParams.Distribution.Uniform )
            generator = new UniformGenerator();
        else if( distribution == ProgramParams.Distribution.Gauss )
            generator = new GaussianGenerator();
        else if( distribution == ProgramParams.Distribution.Laplace ) {
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

            Logger.log("Writing generated data to a file: " + System.getProperty("user.dir") + "/" + outputFileName);
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
        LZWEncoder lzwEncoder = new LZWEncoder();
        byte[] encodedData  = lzwEncoder.encode(inputData);

        //  end timer
        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        //  compute avg bit length
//        double avgBitLength = AvgBitLengthUtils.computeAvgBitLength(inputData, Byte.toUnsignedInt(encodedData[0]), lzwEncoder.getIndexes(), lzwEncoder.getDictionary());
        double avgBitLength = AvgBitLengthUtils.computeAvgBitLength(/*inputData, */Byte.toUnsignedInt(encodedData[0]), lzwEncoder.getIndexes(), lzwEncoder.getDictionary());

        //  log results
        Logger.testLog(
                ProgramParams.getInputFileName() +
                        " | " + (1 - ((float) encodedData.length / inputData.length)) * 100 + "%" +
                        " | " + String.format("%.2f", inputEntropy) +
                        " | " + String.format("%.2f", avgBitLength)
        );
        Logger.log("Entropy vs avg bit length: " + String.format("%.2f", inputEntropy) + " " + String.format("%.2f", avgBitLength));

        //  write encoded data to output file
        writeOutput(outputFileName, encodedData);

        //  log time
        Logger.log("Operation lasted " + (float) elapsedMs / 1000 + "s");

    }

    private static void decode() throws InvalidParamsException, InvalidInputEncodedDataFileException {

        //  get output file name
        String outputFileName = requiresOutputFileName();

        //  input file
        byte[] inputData = requiresInputFile();

        //  start timer
        long startTimeMs = System.currentTimeMillis();

        //  decode data
        byte[] decodedData = new LZWDecoder().decode(inputData);

        //  end timer
        long elapsedMs = System.currentTimeMillis() - startTimeMs;

        //  write decoded data to output file
        if(decodedData != null) {
            writeOutput(outputFileName, decodedData);
        } else {
            System.out.println("No data to write");
            System.exit(1);
        }

        //  log time
        Logger.log("Operation lasted " + (float) elapsedMs / 1000 + "s");

    }

    private static byte[] requiresInputFile() throws InvalidParamsException {
        String inputFileName = ProgramParams.getInputFileName();
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
        String outputFileName = ProgramParams.getOutputFileName();
        if(outputFileName != null) {
            return outputFileName;
        } else {
            throw new InvalidParamsException("Output file needs to be defined");
        }
    }

    private static ProgramParams.Distribution requiresDistribution() throws InvalidParamsException {
        ProgramParams.Distribution distribution = ProgramParams.getDistribution();
        if(distribution == ProgramParams.Distribution.Unknown || distribution == null) {
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
