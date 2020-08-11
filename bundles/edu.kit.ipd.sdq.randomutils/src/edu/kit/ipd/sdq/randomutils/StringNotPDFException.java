package edu.kit.ipd.sdq.randomutils;

import de.uka.ipd.sdq.probfunction.math.exception.ProbabilityFunctionException;

public class StringNotPDFException extends ProbabilityFunctionException {

    private static final long serialVersionUID = -255350298326225894L;

    public StringNotPDFException() {
        super();
    }

    public StringNotPDFException(String string) {
        super(string);
    }

}
