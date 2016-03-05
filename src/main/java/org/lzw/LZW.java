package org.lzw;

import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lukasz on 03.03.16.
 */
public final class LZW {

    public static byte[] encode(final byte[] data) {
        if(data == null) {
            throw new IllegalArgumentException("Data to encode can not be null");
        }

        if(data.length == 0) {
            throw new IllegalArgumentException("Data to encode can not be empty");
        }

        /** lets start encoding    **/

        LinkedList<Integer> indexes = new LinkedList<>();

        //  fill dict with source data alphabet
        Dictionary dictionary = createAlphabet();

        //  read first symbol
        LinkedList<Byte> c = new LinkedList<>();
        c.add(data[0]);

        int i = 1;
        while (i < data.length) {   //  while data in input

            //  read s symbol
            byte s = data[i];

//            if(contains(dictionary, c, s)) {
            if(dictionary.contains(c, s)) {
                //   c := c + s
                c.add(s);
            } else {
                //  push code(index) of c to output
                indexes.add(dictionary.getIndexOf(c) + 1);

                //  add c + s to dictionary
                dictionary.add(c, s);

                //  c := s
                c.clear();
                c.add(s);
            }
            ++i;
        }

        //  int the end, push code(index) of c to output
        indexes.add(dictionary.getIndexOf(c) + 1);

        return indexesToByteArray(indexes);
    }

    public static byte[] decode(byte[] encodedData) {

        if(encodedData == null) {
            throw new IllegalArgumentException("Encoded data can not be null");
        }

        if(encodedData.length == 0) {
            return new byte[0];
        }

        //  get indexes
        int[] indexes = getIndexes(encodedData);

        /** lets start decoding **/

        LinkedList<byte[]> result = new LinkedList<>();

        //  fill dict with source data alphabet //  TODO decide whether to use encodedData dict or i.e. ascii table
        Dictionary dictionary = createAlphabet();

        //  pk := first code of compressed data
        int pk = indexes[0] - 1;

        //  push symbol related to pk to output
        result.add(dictionary.getWord(pk));

        int i = 1;
        while(i < indexes.length) { //  while there are still code words to process
            //  read k code
            int k = indexes[i] - 1;

            // pc := dict[pk]
            byte[] pc = dictionary.getWord(pk);
            assert(pc != null);

            //  kword
            byte[] kword = dictionary.getWord(k);
            if(kword != null) { // kword is in dict

                //  add {pc + kword[0]} word to dict

                byte[] temp = new byte[pc.length + 1];
                System.arraycopy(pc, 0, temp, 0, pc.length);
                temp[temp.length - 1] = kword[0];
                dictionary.add(temp);

                //  push kword to output
                result.add(kword);
            } else {
                //  add {pc + pc[0]} word to dict
                byte[] temp = new byte[pc.length + 1];
                System.arraycopy(pc, 0, temp, 0, pc.length);
                temp[temp.length - 1] = pc[0];
                dictionary.add(temp);

                //  push {pc + pc[0]} to output
                result.add(temp);
            }

            //  pk := k
            pk = k;

            ++i;
        }

        return toArray(result);
    }

    private static Dictionary createAlphabet() {
        return new Dictionary();    //  Dictionary is alphabet-aware now (thats an ascii alphabet)
    }

    static byte[] indexesToByteArray(final List<Integer> indexes) {
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

        System.out.println("Encoding " + indexes.size() + " indexes with " + bitsPerInt + " bits per index");

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
        byte[] result = new byte[encodedByteArray.length + 1];  //TODO do it more effectively
        result[0] = (byte)bitsPerInt;
        System.arraycopy(encodedByteArray, 0, result, 1, encodedByteArray.length);
        return result;
    }

    static int[] getIndexes(final byte[] data) {
        assert(data.length >= 2);   //  TODO remove assert
        int bitsPerInt = data[0];
        if(bitsPerInt < 0) {
            throw new IllegalArgumentException("Number of bits per int must be > 0");
        }
        byte[] intsByteArray = Arrays.copyOfRange(data, 1, data.length);    //  TODO find more efficient way
        return intsFromByteArray(intsByteArray, bitsPerInt);
    }

    static int[] intsFromByteArray(final byte[] indexesByteArray, final int bitsPerInt) {

        int[] resultInts = new int[ (indexesByteArray.length * 8) / bitsPerInt];

        BitSet bitSet = BitSet.valueOf(indexesByteArray);
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

    private static byte[] toArray(LinkedList<byte[]> list) {

        int lengthSum = 0;
        for(byte[] element : list) {
            lengthSum += element.length;
        }

        byte[] result = new byte[lengthSum];
        int i = 0;
        for(byte[] element : list) {
            System.arraycopy(element, 0, result, i, element.length);
            i += element.length;
        }
        return result;
    }

}
