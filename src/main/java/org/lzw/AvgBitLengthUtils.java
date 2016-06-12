package org.lzw;

import org.lzw.algorithm.EncodingDictionary;

import java.util.*;

/**
 * Created by lukasz on 12.06.16.
 */
public class AvgBitLengthUtils {

//    public static double computeAvgBitLength(byte[] inputData, int indexBitLength, List<Integer> encodedDataIndexes, EncodingDictionary encodingDictionary) {
//        Map<Byte, Integer> inputDataStats = DataStatisticsUtils.computeStatistics(inputData);
//
//        List<List<Byte>> encodingDictionaryEntries = new ArrayList<>();
//        for(Map.Entry<Integer, List<Byte>> a : encodingDictionary.getReversedInternalDictionary().entrySet()) {
//            encodingDictionaryEntries.add(a.getValue());
//        }
//
//        double denominator = 0.0d;
//        for(List<Byte> encodingDictionaryEntry : encodingDictionaryEntries) {
//            if(encodingDictionaryEntry.isEmpty()) {
//                throw new IllegalArgumentException();
//            }
//            double entryBytesProbabilityProduct = 1.0d;
//            for(Byte b : encodingDictionaryEntry) {
//                entryBytesProbabilityProduct *= ((double)inputDataStats.getOrDefault(b, 0) / inputData.length);
//            }
//            denominator += encodingDictionaryEntry.size() * entryBytesProbabilityProduct;
//        }
//        if(denominator == 0.0d) {
//            throw new IllegalArgumentException();
//        }
//        return (double)indexBitLength/denominator;
//    }

    public static double computeAvgBitLength(/*byte[] inputData, */int indexBitLength, List<Integer> encodedDataIndexes, EncodingDictionary encodingDictionary) {
        double result = 0.0d;
        for(Integer encodedDataIndex : encodedDataIndexes) {
            result += (double)indexBitLength / encodingDictionary.get(encodedDataIndex).size();
        }
        result /= encodedDataIndexes.size();
        return result;
    }

}
