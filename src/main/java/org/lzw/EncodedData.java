package org.lzw;

import java.util.Arrays;

/**
 * Created by lukasz on 03.03.16.
 */
public class EncodedData {

    public int[] indexes;   //  WARNING! these indexes dont start with 0. They DO start with 1.
    public Dictionary dictionary;

    public EncodedData() {
        indexes = new int[0];
        dictionary = new Dictionary();
    }

    public EncodedData(int[] indexes, Dictionary dictionary) {
        this.indexes = indexes;
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        return "indexes: " + Arrays.toString(indexes);
    }
}
