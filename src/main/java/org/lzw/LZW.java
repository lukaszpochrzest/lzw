package org.lzw;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by lukasz on 03.03.16.
 */
public final class LZW {

    public static final String WIKI_DATA_EXAMPLE = "abccd_abccd_acd_acd_acd_";
//    public static final String WIKI_DATA_EXAMPLE_ENCODED = "1, 2, 3, 3, 4, 5, 6, 8, 10, 1, 9, 11, 16, 15, 10"

    public static final PrintStream out = System.out;

    public static int[] encode(final byte[] data) {
        if(data == null) {
            return null;
        }

        if(data.length == 0) {
            return new int[0];
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

        //  rewrite indexes list to an array

        int[] indexesArray = new int[indexes.size()];
        int ii = 0;
        for(int index : indexes) {
            indexesArray[ii] = index;
            ++ii;
        }

        return indexesArray;
    }

    public static byte[] decode(int[] indexes) {

        if(indexes == null) {
            return null;
        }

        if(indexes.length == 0) {
            return new byte[0];
        }

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


    public static void main (String[] args) {


        /** wiki example */

        String dataString = WIKI_DATA_EXAMPLE;

        /** encoding **/

        out.println("Encoding: " + dataString);

        byte[] data = dataString.getBytes(StandardCharsets.US_ASCII);
        int[] indexes = LZW.encode(data);

        out.println("Result:" + System.lineSeparator() +
                "\t" + Arrays.toString(indexes) + System.lineSeparator());

        /** decoding **/

        out.println("Decoding: " + Arrays.toString(indexes));

        byte[] decodedData = LZW.decode(indexes);

        out.println("Result:" + new String(decodedData, StandardCharsets.US_ASCII) + System.lineSeparator());

//        /** this works! - prints 'a'*/
//        int i = 97; //  ascii 'a'
//        byte b = (byte)i;
//        System.out.println(new String(new byte[] {b}, StandardCharsets.UTF_8));
    }



}
