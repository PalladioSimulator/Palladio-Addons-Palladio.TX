package edu.kit.ipd.sdq.randomutils.factories;

import java.util.List;

import edu.kit.ipd.sdq.randomutils.RandomVariable;
import edu.kit.ipd.sdq.randomutils.StoExRandomVariable;
import edu.kit.ipd.sdq.randomutils.StringRandomVariable;

public class EmpiricalDistribution {

	/**
	 * Builds a random variable of Double values according to the specified UML
	 * SPT histogram.
	 * <p>
	 * SPT histogram example: {@code 0.0, 0.3, 1.0, 0.2, 1.5, 0.5, 2.0}
	 * <p>
	 * For the given SPT histogram, the random variable will give
	 * <ul>
	 * <li>a value between 0.0 and 1.0 with a 30 percent probability</li>
	 * <li>a value between 1.0 and 1.5 with a 20 percent probability</li>
	 * <li>a value between 1.5 and 2.0 with a 50 percent probability</li>
	 * </ul>
	 * 
	 * @see http://www.omg.org/cgi-bin/doc.cgi?formal/2005-01-02.pdf
	 * 
	 * @param sptHistogram
	 * @return the random variable
	 */
	public static RandomVariable<Double> fromSPTHistogram(double... sptHistogram) {
		return StoExRandomVariable.fromSPTHistogram(sptHistogram);
	}
	

	/**
	 * Builds a random variable of Double values according to the specified
	 * Stochastic Expression (StoEx).
	 * <p>
	 * StoEx example: {@code DoublePDF[(1.0;0.3)(1.5;0.2)(2.0;0.5)]}
	 * <p>
	 * For the given StoEx, the random variable will give
	 * <ul>
	 * <li>a value between 0.0 and 1.0 with a 30 percent probability</li>
	 * <li>a value between 1.0 and 1.5 with a 20 percent probability</li>
	 * <li>a value between 1.5 and 2.0 with a 50 percent probability</li>
	 * </ul>
	 * 
	 * @see http://digbib.ubka.uni-karlsruhe.de/volltexte/documents/1670337
	 * 
	 * @param stoEx
	 *            the stochastic expression
	 *            {@code DoublePDF[(x;p)(y;q)...(z;r)]}, with p, q and r being
	 *            probabilities.
	 * @return the random variable
	 */
	public static RandomVariable<Double> fromStoEx(String stoEx) {
		return new StoExRandomVariable(stoEx);
	}
	
	public static <T> ListDistributionBuilder<T> fromList(T... list) {
		return new ListDistributionBuilder<T>(list);
	}
	
	public static <T> ListDistributionBuilder<T> fromList(List<T> list) {
		return new ListDistributionBuilder<T>(list);
	}
	
	public static RandomVariable<String> string(int min, int max) {
		return new StringRandomVariable(TheoreticalDistribution.uniformInt(min, max));
	}

}
