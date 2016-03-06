package org.lzw;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukasz on 04.03.16.
 */
public class ProgramParams {


    private static final String HELP = "-h";
    private boolean help = false;

    private static final String INPUT = "-i";
    private String inputFileName;

    private static final String OUTPUT = "-o";
    private String outputFileName;

    private static final String ENCODE = "encode";
    private boolean encode = false;

    private static final String DECODE = "decode";
    private boolean decode = false;

    private static final String VERBOSE = "-v";
    private boolean verbose = false;


    public static String paramsDescription() {
        return  INPUT + "\t" + "input file" + System.lineSeparator() +
                OUTPUT + "\t" + "output file" + System.lineSeparator() +
                ENCODE + "\t" + "encode" + System.lineSeparator() +
                DECODE + "\t" + "decode" + System.lineSeparator() +
                VERBOSE + "\t" + "verbose" + System.lineSeparator();
    }

    private static ProgramParams pp;

    public static void parse(String[] args) {
        pp = new ProgramParams(args);
    }

    private ProgramParams(String[] args) {
        List<String> argList = Arrays.asList(args);
        help(argList);
        input(argList);
        output(argList);
        encode(argList);
        decode(argList);
        verbose(argList);
    }

    /**
     * Parsing help option
     */
    private void help(List<String> argList) {
        help = argList.indexOf(HELP) != -1;
    }

    /**
     * Parsing -i option
     */
    private void input(List<String> argList) {
        int index = argList.indexOf(INPUT);
        if(index != -1) {
            if(index + 1 < argList.size()) {
                inputFileName = argList.get(index + 1);
            }
        }
    }

    /**
     * Parsing -o option
     */
    private void output(List<String> argList) {
        int index = argList.indexOf(OUTPUT);
        if(index != -1) {
            if(index + 1 < argList.size()) {
                outputFileName = argList.get(index + 1);
            }
        }
    }

    /**
     * Parsing encode option
     */
    private void encode(List<String> argList) {
        encode = argList.indexOf(ENCODE) != -1;
    }

    /**
     * Parsing decode option
     */
    private void decode(List<String> argList) {
        decode = argList.indexOf(DECODE) != -1;
    }

    private void verbose(List<String> argList) {
        verbose = argList.indexOf(VERBOSE) != -1;
    }

    public static String getInputFileName() {
        return pp != null ? pp.inputFileName : null;
    }

    public static String getOutputFileName() {
        return pp != null ? pp.outputFileName : null;
    }

    public static boolean isEncode() {
        return pp != null ? pp.encode : false;
    }

    public static boolean isDecode() {
        return pp != null ? pp.decode : false;
    }

    public static boolean isHelp() {
        return pp != null ? pp.help : false;
    }

    public static boolean isVerbose() {
        return pp != null ? pp.verbose : false;
    }
}
