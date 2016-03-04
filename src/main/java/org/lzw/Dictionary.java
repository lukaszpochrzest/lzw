package org.lzw;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by lukasz on 03.03.16.
 */

/**
 * Dictionary in terms of LZW algorithm
 */
public final class Dictionary extends LinkedList<byte[]> {


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

}
