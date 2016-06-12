package org.lzw;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukasz on 12.06.16.
 */
public class DataStatisticsUtils {

    public static Map<Byte, Integer> computeStatistics (byte[] data) {
        Map<Byte, Integer> stats = new HashMap<>();
        for(byte b : data) {
            Integer count = stats.get(b);
            if(count != null) {
                stats.put(b, count + 1);
            } else {
                stats.put(b, 1);
            }
        }
        return stats;
    }

}
