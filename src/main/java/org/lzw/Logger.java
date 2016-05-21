package org.lzw;

import static org.lzw.ProgramParams.isVerbose;
import static org.lzw.ProgramParams.isTestLog;

/**
 * Created by lukasz on 06.03.16.
 */
public class Logger {

    public static void log(String log) {
        if(isVerbose()) {
            System.out.println(log);
        }
    }

    public static void testLog(String log) {
        if(isTestLog()) {
            System.out.println(log);
        }
    }

}
