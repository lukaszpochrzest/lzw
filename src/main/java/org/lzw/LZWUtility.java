package org.lzw;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.lzw.Logger.log;

/**
 * Created by lukasz on 06.03.16.
 */
public class LZWUtility {

    public static byte[] indexesToByteArray(final List<Integer> indexes) {
        assert(indexes.size() > 0); //  TODO remove assert

        //  find out greatest index value
        Integer maxIndex = Integer.MIN_VALUE;
        for(Integer index : indexes) {
            if (index > maxIndex) {
                maxIndex = index;
            }
        }

        //  find out number bits we have to use to write indexes in optimal way
        int bitsPerInt = 0;
        while (maxIndex > 0) {
            bitsPerInt++;
            maxIndex = maxIndex >> 1;
        }

        assert(bitsPerInt > 0); //  TODO remove assert

        log("Encoding " + indexes.size() + " indexes with " + bitsPerInt + " bits per index");

        //  lets write ints to bytearray in optimal way
        BitSet bitSet = new BitSet(indexes.size() * bitsPerInt);

        int bitSetInd = 0;
        boolean[] tempBits = new boolean[bitsPerInt];
        for(Integer index : indexes) {
            for (int i = bitsPerInt - 1; i >= 0; --i) {
                boolean bit = (index & (1 << i)) != 0;  //  TODO check if it works in both LittleEndian and BigEndian?
                tempBits[i] = bit;
            }
            for(int k = 0; k < tempBits.length; ++k) {
                bitSet.set(bitSetInd + k, tempBits[k]);
            }
            bitSetInd += tempBits.length;
        }

        byte[] encodedByteArray = bitSet.toByteArray();
        byte[] result = new byte[encodedByteArray.length + 1];  //  TODO do it more efficient way
        result[0] = (byte)bitsPerInt;
        System.arraycopy(encodedByteArray, 0, result, 1, encodedByteArray.length);
        return result;
    }

    public static int[] indexesFromByteArray(final byte[] data) {
        assert(data.length >= 2);   //  TODO remove assert
        int bitsPerInt = data[0];
        if(bitsPerInt < 0) {
            throw new IllegalArgumentException("Number of bits per int must be > 0");
        }
        byte[] intsByteArray = Arrays.copyOfRange(data, 1, data.length);    //  TODO find more efficient way
        return indexesFromPrunedByteArray(intsByteArray, bitsPerInt);
    }

    static int[] indexesFromPrunedByteArray(final byte[] intsByteArray, final int bitsPerInt) {

        int[] resultInts = new int[ (intsByteArray.length * 8) / bitsPerInt];

        BitSet bitSet = BitSet.valueOf(intsByteArray);
        int bitSetInd = 0;
        int resultIntsInd = 0;
        while(resultIntsInd < resultInts.length) {

            int newInt = 0;
            for(int newIntInd = 0; newIntInd < bitsPerInt; ++newIntInd) {
                newInt += bitSet.get(newIntInd + bitSetInd) ? (1 << newIntInd) : 0; //  TODO check if it works in both LittleEndian and BigEndian?
            }
            resultInts[resultIntsInd++] = newInt;

            bitSetInd += bitsPerInt;
        }

        return resultInts;
    }

    public static byte[] toArray(List<List<Byte>> list) {

        int lengthSum = 0;
        for(List element : list) {
            lengthSum += element.size();
        }

        byte[] result = new byte[lengthSum];

        int i = 0;
        for(List<Byte> element : list) {
//            System.arraycopy(element, 0, result, i, element.length);
//            i += element.length;
            int k = 0;
            for(Byte b : element) {
                result[i+k] = b;
                ++k;
            }
            i += element.size();
        }
        return result;
    }

}
