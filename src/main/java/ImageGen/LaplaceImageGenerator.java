package ImageGen;

import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Witek on 2016-05-01.
 */


public class LaplaceImageGenerator extends ImageGenerator
{
    double mu;
    double beta;

    public LaplaceImageGenerator(double paramMu, double paramBeta) {
        this.mu = paramMu;
        this.beta = paramBeta;
    }

    @Override
    int GetNextSample( int x, int y ) {
        double randValue = ThreadLocalRandom.current().nextDouble(0, 256);

        if( beta < 0 ){
            System.err.println("Invalid value of Beta! Should be positive therefore generated imaged is invalid.");
        }

        if( x <= mu ) {
            return (int)(Math.exp((randValue - mu) / beta ) / 2.0);
        } else {
            return (int)(1.0 - Math.exp((randValue - mu) / beta ) / 2.0);
        }
    }
}
