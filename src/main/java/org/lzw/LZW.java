package org.lzw;

import static org.lzw.Logger.log;
import static org.lzw.LZWUtility.*;

import java.util.*;

/**
 * Created by lukasz on 03.03.16.
 */
public final class LZW {

    public static byte[] encode(final byte[] data) {
        if(data == null) {
            throw new IllegalArgumentException("Data to encode can not be null");
        }

        if(data.length == 0) {
            return new byte[0];
        }

        /** lets start encoding    **/

        LinkedList<Integer> indexes = new LinkedList<>();

        //  fill dict with source data alphabet
        EncodingDictionary dictionary = new EncodingDictionary();

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

        byte[] resultByteArray = indexesToByteArray(indexes);

        log("Compressed " + data.length + " bytes to " + resultByteArray.length + " bytes" +
                (resultByteArray.length < data.length ? ", space saving " + (1 - ((float) resultByteArray.length/data.length))*100 + "%" : ""));

        return resultByteArray;
    }

    public static byte[] decode(byte[] encodedData) {

        if(encodedData == null) {
            throw new IllegalArgumentException("Encoded data can not be null");
        }

        if(encodedData.length == 0) {
            return new byte[0];
        }

        //  get indexes
        int[] indexes = indexesFromByteArray(encodedData);

        /** lets start decoding **/

        LinkedList<List<Byte>> result = new LinkedList<>();

        //  fill dict with source data alphabet //  TODO decide whether to use encodedData dict or i.e. ascii table
        DecodingDictionary decodingDictionary = new DecodingDictionary();

        //  pk := first code of compressed data
        int pk = indexes[0] - 1;

        //  push symbol related to pk to output
        result.add(decodingDictionary.getWord(pk));

        int i = 1;
        while(i < indexes.length) { //  while there are still code words to process
            //  read k code
            int k = indexes[i] - 1;

            // pc := dict[pk]
            List<Byte> pc = decodingDictionary.getWord(pk);
            assert(pc != null);

            //  kword
            List<Byte> kword = decodingDictionary.getWord(k);
            if(kword != null) { // kword is in dict

                //  add {pc + kword[0]} word to dict
                List<Byte> temp = new LinkedList<>(pc);
                temp.add(kword.get(0));

                decodingDictionary.add(temp);

                //  push kword to output
                result.add(kword);
            } else {
                //  add {pc + pc[0]} word to dict

                List<Byte> temp = new LinkedList<>(pc);
                temp.add(pc.get(0));

                decodingDictionary.add(temp);

                //  push {pc + pc[0]} to output
                result.add(temp);
            }

            //  pk := k
            pk = k;

            ++i;
        }

        byte[] resultByteArray = toArray(result);

        log("Decompressed " + encodedData.length + " bytes to " + resultByteArray.length + " bytes");

        return resultByteArray;
    }

}
