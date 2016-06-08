package org.gen;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Witek on 2016-05-01.
 */


public class GaussianGenerator extends Generator
{

    @Override
    int GetNextSample(int x, int y)
    {
        // Gauss distribution with mean 0.0 and standard deviation 1.0
        double randomValue = ThreadLocalRandom.current().nextGaussian();
        // Set mean to 128.
        randomValue = ( randomValue + 128.0 );
        // Set standard deviations. Note: when standard deviation is 1.0 then
        // for values above 3.0 we have almost zero probability. That's why I make
        // this division by 3.0
        randomValue = randomValue * ( 128.0 / 3.0 );

        return ((int) randomValue);
    }
}
