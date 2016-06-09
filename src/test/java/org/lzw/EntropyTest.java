package org.lzw;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lukasz on 08.06.16.
 */
public class EntropyTest {

    private final byte[] data = {
            1, 1, 1, 1, 1, 1,                   //  6
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //  12
            3, 3, 3, 3,                         //  4
            4, 4, 4, 4, 4,                      //  5
            5, 5, 5, 5,                         //  4
    };

    private final double entropy = 2.1755200043411453;

    @Test
    public void entropyTest() {
        Assert.assertEquals(EntropyUtils.computeEntropy(data), entropy, 0.0d);
    }
}
