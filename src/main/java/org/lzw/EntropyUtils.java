package org.lzw;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukasz on 08.06.16.
 */
public class EntropyUtils {

    public static double computeEntropy(byte[] data) {
        if(data == null) {
            throw new IllegalArgumentException();
        }

        HashMap<Byte, Integer> dataStats = new HashMap<>();

        // create data stats
        for(byte b : data) {
            add(b, dataStats);
        }

        //  compute entropy
        double entropy = 0.0d;
        for(Map.Entry<Byte, Integer> byteStat : dataStats.entrySet()) {
            double probability = (double)byteStat.getValue()/data.length;
            entropy += (-probability) * log2(probability);
        }
        return entropy;
    }

    private static void add(byte b, Map<Byte, Integer> dataStats) {
        Integer count = dataStats.get(b);
        if(count != null) {
            dataStats.put(b, count + 1);
        } else {
            dataStats.put(b, 1);
        }
    }

    private static double log2(double i) {
        return Math.log(i)/Math.log(2);
    }

}
