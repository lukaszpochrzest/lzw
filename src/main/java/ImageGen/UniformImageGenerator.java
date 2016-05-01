package ImageGen;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by Witek on 2016-05-01.
 */


import java.util.concurrent.ThreadLocalRandom;


public class UniformImageGenerator extends ImageGenerator
{

    @Override
    int GetNextSample(int x, int y)
    {
        return ThreadLocalRandom.current().nextInt(0, 256 );
    }
}
