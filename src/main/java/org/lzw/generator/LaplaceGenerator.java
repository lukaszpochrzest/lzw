package org.lzw.generator;

import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.signum;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Witek on 2016-05-01.
 */
public class LaplaceGenerator extends Generator
{
    private static final double DEFAULT_MU = 128.0d;
    private static final double DEFAULT_BETA = 10.0d;

    private double mu;
    private double beta;

    public LaplaceGenerator() {
        mu = DEFAULT_MU;
        beta = DEFAULT_BETA;
    }

    public LaplaceGenerator(double paramMu, double paramBeta) {
        this.mu = paramMu;
        this.beta = paramBeta;
    }

    /**
     * way to generate random variable according to Laplace distribution
     *
     * https://en.wikipedia.org/wiki/Laplace_distribution#Generating_random_variables_according_to_the_Laplace_distribution
     */
    @Override
    int GetNextSample( int x, int y ) {
        float randValue = ThreadLocalRandom.current().nextFloat() - 0.5f;
        int xx = (int)(mu - beta * signum(randValue) * log(1 - 2 * abs(randValue)));
        return xx;
    }
}
