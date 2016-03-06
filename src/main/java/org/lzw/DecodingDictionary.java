package org.lzw;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by lukasz on 03.03.16.
 */

/**
 * Dictionary in terms of LZW algorithm
 */
public final class DecodingDictionary {

    protected static final int INITIAL_INDEX = 256;

    /**
     * contains only more-than-one-byte words
     */
    private LinkedList<byte[]> dictionaryInternal = new LinkedList<>();


    public boolean add(LinkedList<Byte> c, byte s) {
        /** assuming c is not empty (thats how algorithm works) **/

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

    public boolean add(byte[] word) {
        return dictionaryInternal.add(word);
    }

    int getIndexOf(LinkedList<Byte> word) {
        if(word.size() == 1) {
            return Byte.toUnsignedInt(word.get(0));
        }
        int dictInd = 0;
        for(byte[] wordDict : dictionaryInternal) {
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
                    return dictInd + INITIAL_INDEX;
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

        if(index < INITIAL_INDEX) { //  ascii symbol requested
            return new byte[] {(byte)index};
        }

        return dictionaryInternal.get(index - INITIAL_INDEX);
    }

    public boolean contains(final LinkedList<Byte> c, final byte s) {
        /** assuming c is not empty (thats how algorithm works) **/

        for(byte[] word : dictionaryInternal) {
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
        return dictionaryInternal.size() + INITIAL_INDEX;
    }

}
