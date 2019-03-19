package edu.kit.ipd.sdq.randomutils.factories;

import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;

import edu.kit.ipd.sdq.randomutils.RandomVariable;

public class TheoreticalDistribution {

    public static RandomVariable<Double> uniform(double lower, double upper) {
        return adapt(new UniformRealDistribution(lower, upper));
    }

    public static RandomVariable<Integer> uniformInt(int lower, int upper) {
        return adapt(new UniformIntegerDistribution(lower, upper));
    }

    public static RandomVariable<Integer> zipf(int numberOfElements, double exponent) {
        // TODO document the -1 part
        return minusOne(adapt(new ZipfDistribution(numberOfElements, exponent)));
    }

    /**
     * @param mean
     *            the expected value, or in case of an arrival process the expected interarrival
     *            time
     */
    public static RandomVariable<Double> exponential(double mean) {
        return adapt(new ExponentialDistribution(mean));
    }

    /**
     * @param mean
     *            the expected value
     */
    public static RandomVariable<Integer> poisson(double mean) {
        return adapt(new PoissonDistribution(mean));
    }

    /**
     * @param mean
     *            the expected value
     * @param sd
     *            the standard deviation
     */
    public static RandomVariable<Double> normal(double mean, double sd) {
        return adapt(new NormalDistribution(mean, sd));
    }

    private static RandomVariable<Integer> minusOne(final RandomVariable<Integer> decorated) {
        return new RandomVariable<Integer>() {
            @Override
            public Integer next() {
                return decorated.next() - 1;
            }
        };
    }

    private static RandomVariable<Integer> adapt(final AbstractIntegerDistribution distribution) {
        return new RandomVariable<Integer>() {
            @Override
            public Integer next() {
                return distribution.sample();
            }
        };
    }

    private static RandomVariable<Double> adapt(final AbstractRealDistribution distribution) {
        return new RandomVariable<Double>() {
            @Override
            public Double next() {
                return distribution.sample();
            }
        };
    }

}
