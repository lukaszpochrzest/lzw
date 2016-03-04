package org.lzw;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by lukasz on 03.03.16.
 */

/**
 * Dictionary in terms of LZW algorithm
 */
//public final class Dictionary extends LinkedList<byte[]> {
//
//
//    public boolean add(LinkedList<Byte> c, byte s) {
//        byte[] newWord = new byte[c.size() + 1];
//
//        //  rewrite bytes from c to newWord
//        int i = 0;
//        for(byte b : c) {
//            newWord[i] = b;
//            ++i;
//        }
//
//        //  push s to newWord
//        newWord[newWord.length - 1] = s;
//
//        //  add newWord to dict
//        return add(newWord);
//    }
//
//    int getIndexOf(LinkedList<Byte> word) {
//        int dictInd = 0;
//        for(byte[] wordDict : this) {
//            if(word.size() == wordDict.length) {
//                boolean equal = true;
//
//                Iterator<Byte> wordIter = word.iterator();
//                int wordDictInd = 0;
//                while(wordIter.hasNext()) {
//                    Byte wordC = wordIter.next();
//                    byte wordDictC = wordDict[wordDictInd];
//                    if(wordDictC != wordC) {
//                        equal = false;
//                        break;
//                    }
//                    ++wordDictInd;
//                }
//                if(equal) {
//                    return dictInd;
//                }
//            }
//            ++dictInd;
//        }
//        throw new RuntimeException("This is an internal error." +
//                " Could not find word: " + word.toArray().toString() +
//                " in dictionary");
//    }
//
//    byte[] getWord(int index) {
//        if(index >= size()) {
//            return null;
//        }
//        return get(index);
//    }
//
//}

public final class Dictionary implements Iterable<byte[]> {

    private LinkedList<byte[]> dictionaryInternal = new LinkedList<>();


    public boolean add(LinkedList<Byte> c, byte s) {
        byte[] newWord = new byte[c.size() + 1];

        //  rewrite bytes from c to newWord
        int i = 0;
        for(byte b : c) {
            newWord[i] = b;
            ++i;
        }

        //  push s to newWord
        newWord[newWord.length - 1] = s;

        //  add newWord to dict
        return add(newWord);
    }

    public boolean add(byte[] e) {
        return dictionaryInternal.add(e);
    }

    int getIndexOf(LinkedList<Byte> word) {
        int dictInd = 0;
        for(byte[] wordDict : this) {
            if(word.size() == wordDict.length) {
                boolean equal = true;

                Iterator<Byte> wordIter = word.iterator();
                int wordDictInd = 0;
                while(wordIter.hasNext()) {
                    Byte wordC = wordIter.next();
                    byte wordDictC = wordDict[wordDictInd];
                    if(wordDictC != wordC) {
                        equal = false;
                        break;
                    }
                    ++wordDictInd;
                }
                if(equal) {
                    return dictInd;
                }
            }
            ++dictInd;
        }
        throw new RuntimeException("This is an internal error." +
                " Could not find word: " + word.toArray().toString() +
                " in dictionary");
    }

    byte[] getWord(int index) {
        if(index >= size()) {
            return null;
        }
        return get(index);
    }

    public boolean contains(final LinkedList<Byte> c, final byte s) {
        for(byte[] word : this) {
            if(c.size() + 1 == word.length) {
                boolean equalUpToS = true;

                Iterator<Byte> sequenceIter = c.iterator();
                int wordInd = 0;
                while(sequenceIter.hasNext()) {
                    Byte seqC = sequenceIter.next();
                    byte wordC = word[wordInd];
                    if(wordC != seqC) {
                        equalUpToS = false;
                        break;
                    }
                    ++wordInd;
                }
                if(equalUpToS) {
                    if(word[wordInd] == s) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int size() {
        return dictionaryInternal.size();
    }

    public byte[] get(int index) {
        return dictionaryInternal.get(index);
    }

    @Override
    public Iterator iterator() {
        return dictionaryInternal.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        dictionaryInternal.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return dictionaryInternal.spliterator();
    }

}
