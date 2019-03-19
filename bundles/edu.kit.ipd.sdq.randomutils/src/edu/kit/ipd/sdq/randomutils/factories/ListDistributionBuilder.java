package edu.kit.ipd.sdq.randomutils.factories;

import java.util.Arrays;
import java.util.List;

import edu.kit.ipd.sdq.randomutils.RandomVariable;

public class ListDistributionBuilder<T> {

	private List<T> list;

	public ListDistributionBuilder(T[] list) {
		this(Arrays.asList(list));
	}

	public ListDistributionBuilder(List<T> list) {
		this.list = list;
	}

	private RandomVariable<T> withSelectionDistribution(final RandomVariable<Integer> index) {
		return new RandomVariable<T>() {
			@Override
			public T next() {
				return list.get(index.next());
			}
		};
	}

	public RandomVariable<T> uniform() {
		return withSelectionDistribution(TheoreticalDistribution.uniformInt(0, list.size() - 1));
	}

	public RandomVariable<T> zipf(double exponent) {
		return withSelectionDistribution(TheoreticalDistribution.zipf(list.size(), exponent));
	}

}
