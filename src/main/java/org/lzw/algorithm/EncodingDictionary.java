package org.lzw.algorithm;

import java.util.*;

/**
 * Created by lukasz on 05.03.16.
 */
public class EncodingDictionary {

    private static final int INITIAL_DICTIONARY_INTERNAL_SIZE = 4096;

    protected static final int INITIAL_INDEX = 256;

    private Integer currentIndex = INITIAL_INDEX;

    private HashMap<List<Byte>, Integer> dictionaryInternal = new HashMap<>(INITIAL_DICTIONARY_INTERNAL_SIZE);

    public boolean add(LinkedList<Byte> c, byte s) {

        LinkedList<Byte> newWord = new LinkedList(c);
        newWord.add(s);

        return add(newWord);
    }

    public boolean add(List<Byte> word) {
        dictionaryInternal.put(word, currentIndex++);
        return true;
    }

    int getIndexOf(LinkedList<Byte> word) {

        //  single ascii symbol
        if(word.size() == 1) {
            return Byte.toUnsignedInt(word.get(0));
        }

        //  more than single ascii symbol
        return dictionaryInternal.get(word);
    }


    public boolean contains(LinkedList<Byte> c, byte s) {

        LinkedList<Byte> word = new LinkedList(c);
        word.add(s);

        return dictionaryInternal.containsKey(word);
    }

    public int size() {
        return dictionaryInternal.size();
    }


//    public Set<List<Byte>> getDictionaryEntries() {
//        return dictionaryInternal.keySet();
//    }

    private HashMap<Integer, List<Byte>> reversedInternalDictionary;

    public HashMap<Integer, List<Byte>> getReversedInternalDictionary() {
        if(reversedInternalDictionary == null) {
            reversedInternalDictionary = new HashMap<>();
            for(Map.Entry<List<Byte>, Integer> entry : dictionaryInternal.entrySet()){
                reversedInternalDictionary.put(entry.getValue(), entry.getKey());
            }
            for(Integer i = 0 ; i < 256; ++i) {
                reversedInternalDictionary.put(i, Collections.singletonList(i.byteValue()));
            }
        }
        return reversedInternalDictionary;
    }

    public List<Byte> get(Integer index) {
//        if(index < INITIAL_INDEX) {
//            return Collections.singletonList(index.byteValue());
//        } else {
            return getReversedInternalDictionary().get(index);
//        }
    }
}
