package edu.kit.ipd.sdq.randomutils;

import de.uka.ipd.sdq.probfunction.math.IProbabilityDensityFunction;

public class StoExRandomVariable implements RandomVariable<Double> {

	private IProbabilityDensityFunction pdf;

	public StoExRandomVariable(String stoEx) {
		try {
			pdf = ManagedPDFParser.createFromString(stoEx).getPdfTimeDomain();
		} catch (StringNotPDFException e) {
			throw new RuntimeException(e);
		}
	}

	private StoExRandomVariable(IProbabilityDensityFunction pdf) {
		this.pdf = pdf;
	}

	public static StoExRandomVariable fromSPTHistogram(double[] specification) {
		double minValue = specification[0];
		StringBuilder stoExBuilder = new StringBuilder();
		stoExBuilder.append("DoublePDF[");
		for (int i = 0; i < specification.length - 2; i = i + 2) {
			double probability = specification[i + 1];
			double intervalUpperEndpoint = specification[i + 2] - minValue;
			stoExBuilder.append("(").append(intervalUpperEndpoint).append(";").append(probability).append(")");
		}
		stoExBuilder.append("]");

		IProbabilityDensityFunction pdf = null;
		try {
			pdf = ManagedPDFParser.createFromString(stoExBuilder.toString()).getPdfTimeDomain();
		} catch (StringNotPDFException e) {
			throw new RuntimeException(e);
		}
		pdf = pdf.shiftDomain(minValue); // calculates pdf + minValue
		return new StoExRandomVariable(pdf);
	}

	@Override
	public Double next() {
		return pdf.drawSample();
	}

}
