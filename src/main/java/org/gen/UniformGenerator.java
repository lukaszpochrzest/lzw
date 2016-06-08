package org.gen;

/**
 * Created by Witek on 2016-05-01.
 */


import java.util.concurrent.ThreadLocalRandom;


public class UniformGenerator extends Generator
{

    @Override
    int GetNextSample(int x, int y)
    {
        return ThreadLocalRandom.current().nextInt(0, 256 );
    }
}
