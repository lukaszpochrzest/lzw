package org.lzw.algorithm;

import org.lzw.exception.InvalidParamsException;
import org.lzw.exception.InvalidInputEncodedDataFileException;

import java.util.LinkedList;
import java.util.List;

import static org.lzw.Logger.log;
import static org.lzw.algorithm.LZWUtility.*;

/**
 * Created by lukasz on 03.03.16.
 */
public final class LZW {

    //  package visibility for test purposes
    static LinkedList<Integer> indexes;

    public static byte[] encode(final byte[] data) throws InvalidParamsException {
        if(data == null) {
            throw new InvalidParamsException("Data to encode can not be null");
        }

        if(data.length == 0) {
            throw new InvalidParamsException("Data to encode can not be of 0 byte length");
        }

        /** lets start encoding    **/

        indexes = new LinkedList<>();

        //  fill dict with source data alphabet
        EncodingDictionary dictionary = new EncodingDictionary();

        //  read first symbol
        LinkedList<Byte> c = new LinkedList<>();
        c.add(data[0]);

        int i = 1;
        while (i < data.length) {   //  while data in input

            //  read s symbol
            byte s = data[i];

            if(dictionary.contains(c, s)) {
                //   c := c + s
                c.add(s);
            } else {
                //  push code(index) of c to output
                indexes.add(dictionary.getIndexOf(c));

                //  add c + s to dictionary
                dictionary.add(c, s);

                //  c := s
                c.clear();
                c.add(s);
            }
            ++i;
        }

        //  in the end, push code(index) of c to output
        indexes.add(dictionary.getIndexOf(c));

        byte[] resultByteArray = indexesToByteArray(indexes);

        log("Compressed " + data.length + " bytes to " + resultByteArray.length + " bytes" +
                ", compression percentage = " + (1 - ((float) resultByteArray.length / data.length)) * 100 + "%");

        return resultByteArray;
    }

    public static byte[] decode(byte[] encodedData) throws InvalidParamsException, InvalidInputEncodedDataFileException {

        if(encodedData == null) {
            throw new InvalidParamsException("Encoded data can not be null");
        }

        if(encodedData.length == 0) {
            return new byte[0];
        }

        //  get indexes
        int[] indexes = indexesFromEncodedDataByteArray(encodedData);

        /** lets start decoding **/

        LinkedList<List<Byte>> result = new LinkedList<>();

        //  fill dict with source data alphabet //
        DecodingDictionary decodingDictionary = new DecodingDictionary();

        //  pk := first code of compressed data
        int pk = indexes[0];

        //  push symbol related to pk to output
        result.add(decodingDictionary.getWord(pk));

        int i = 1;
        while(i < indexes.length) { //  while there are still code words to process
            //  read k code
            int k = indexes[i];

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