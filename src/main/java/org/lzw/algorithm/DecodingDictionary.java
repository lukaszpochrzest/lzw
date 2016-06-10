package org.lzw.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lukasz on 03.03.16.
 */

/**
 * Dictionary in terms of LZW algorithm
 */
public final class DecodingDictionary {

    private static final int INITIAL_DICTIONARY_INTERNAL_SIZE = 4096;

    protected static final int INITIAL_INDEX = 256;

    private Integer currentIndex = INITIAL_INDEX;

    private HashMap<Integer, List<Byte>> dictionaryInternal = new HashMap<>(INITIAL_DICTIONARY_INTERNAL_SIZE);

    public boolean add(List<Byte> word) {
        dictionaryInternal.put(currentIndex++, word);
        return true;
    }

    public List<Byte> getWord(int index) {
        if(index >= size()) {
            return null;
        }

        if(index < INITIAL_INDEX) { //  ascii symbol requested
            LinkedList<Byte> result = new LinkedList<>();
            result.add((byte)index);
            return result;
        }

        return dictionaryInternal.get(index /*- INITIAL_INDEX*/);
    }

    public int size() {
        return dictionaryInternal.size() + INITIAL_INDEX;
    }

}
