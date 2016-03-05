package org.lzw;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by lukasz on 04.03.16.
 */
public class LZWTest {

    public static final String WIKI_DATA_EXAMPLE = "abccd_abccd_acd_acd_acd_";
//    public static final String WIKI_DATA_EXAMPLE_ENCODED = "1, 2, 3, 3, 4, 5, 6, 8, 10, 1, 9, 11, 16, 15, 10"

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    @Test
    public void byteArrayIntTest() {

        /** prepare test data   **/
        int[] intArr = new int[] {0, 1, 255, 256, 312, 1033};
        List<Integer> intList = new ArrayList<>(intArr.length);
        for(int ind : intArr) {
            intList.add(ind);
        }

        /** encode indexes  **/
        byte[] encodedData = LZW.indexesToByteArray(intList);

        /** decode indexes  **/
        int bitsPerInt = encodedData[0];
        byte[] encodedDataWithOutFirstByte = new byte[encodedData.length-1];
        System.arraycopy(encodedData, 1, encodedDataWithOutFirstByte, 0, encodedData.length - 1);
        int[] decodedInts = LZW.intsFromByteArray(encodedDataWithOutFirstByte, bitsPerInt);

        Assert.assertTrue(Arrays.equals(intArr, decodedInts));

    }

    @Test
    public void encodeDecodeTest() {

        /** prepare test data   **/
        String dataString = WIKI_DATA_EXAMPLE;
        byte[] data = dataString.getBytes(StandardCharsets.UTF_8);

        /** encoding **/
        byte[] encodedData = LZW.encode(data);

        /** decoding **/
        byte[] decodedData = LZW.decode(encodedData);

        String resultString = new String(decodedData, StandardCharsets.UTF_8);

        Assert.assertTrue(dataString.equals(resultString));

    }

}
