//package org.lzw;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//
///**
// * Created by lukasz on 05.03.16.
// */
//public class Dictionary2 extends Dictionary {
//
//    private static final int INITIAL_DICTIONARY_INTERNAL_SIZE = 1000;
//
//    private Integer currentIndex = INITIAL_INDEX;
//
//    private HashMap<Integer, LinkedList<Byte>> dictionaryInternal = new HashMap<>(INITIAL_DICTIONARY_INTERNAL_SIZE);
//
//    @Override
//    public boolean add(LinkedList<Byte> c, byte s) {
//
//        LinkedList<Byte> newWord = new LinkedList(c);
//        newWord.add(s);
//
//        return add(newWord);
//    }
//
//    @Override
//    public boolean add(byte[] word) {
//        dictionaryInternal.put(currentIndex++, word);
//        return true;
//    }
//
//    @Override
//    int getIndexOf(LinkedList<Byte> word) {
//
//        //  single ascii symbol
//        if(word.size() == 1) {
//            return Byte.toUnsignedInt(word.get(0));
//        }
//
//        byte[] array = word.toArray(new byte[word.size()]);
//
//        //  more than single ascii symbol
//        dictionaryInternal.get()
//
//    }
//
//    @Override
//    byte[] getWord(int index) {
//        return super.getWord(index);
//    }
//
//    @Override
//    public boolean contains(LinkedList<Byte> c, byte s) {
//        return super.contains(c, s);
//    }
//
//    @Override
//    public int size() {
//        return super.size();
//    }
//}
