package edu.kit.ipd.sdq.randomutils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringRandomVariable implements RandomVariable<String> {

	private RandomVariable<Integer> length;

	private boolean letters = true;
	private boolean numbers = true;
	
	public StringRandomVariable(RandomVariable<Integer> length) {
		this(length, true, true);
	}

	public StringRandomVariable(RandomVariable<Integer> length, boolean letters, boolean numbers) {
		this.length = length;
		this.letters = letters;
		this.numbers = numbers;
	}

	@Override
	public String next() {
		return RandomStringUtils.random(length.next(), letters, numbers);
	}

}
