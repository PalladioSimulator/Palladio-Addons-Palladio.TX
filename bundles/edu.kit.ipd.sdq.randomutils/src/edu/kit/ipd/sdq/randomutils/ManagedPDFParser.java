package edu.kit.ipd.sdq.randomutils;

import java.text.ParseException;

import org.palladiosimulator.pcm.stoex.api.StoExParser;

import de.uka.ipd.sdq.probfunction.ProbabilityDensityFunction;
import de.uka.ipd.sdq.probfunction.ProbabilityMassFunction;
import de.uka.ipd.sdq.probfunction.math.ManagedPDF;
import de.uka.ipd.sdq.stoex.Expression;
import de.uka.ipd.sdq.stoex.ProbabilityFunctionLiteral;

public final class ManagedPDFParser {

    protected static final StoExParser STOEX_PARSER = StoExParser.createInstance();

    private ManagedPDFParser() {
        // intentionally left blank
    }

    public static ManagedPDF createFromString(String pdfAsString) throws StringNotPDFException {
        ProbabilityFunctionLiteral value = parse(pdfAsString);

        if (!(value.getFunction_ProbabilityFunctionLiteral() instanceof ProbabilityMassFunction)) {
            throw new StringNotPDFException();
        }
        ProbabilityDensityFunction pdf = (ProbabilityDensityFunction) value.getFunction_ProbabilityFunctionLiteral();
        return new ManagedPDF(pdf);
    }

    private static ProbabilityFunctionLiteral parse(String s) throws StringNotPDFException {
        try {
            int iterInt = Integer.parseInt(s);
            s = "IntPMF[(" + iterInt + ";1.0)]";
        } catch (NumberFormatException e) {
        }

        Expression parsedStoEx;
        try {
            parsedStoEx = STOEX_PARSER.parse(s);
        } catch (ParseException e) {
            throw new StringNotPDFException(e.getMessage());
        }

        if (!(parsedStoEx instanceof ProbabilityFunctionLiteral)) {
            throw new StringNotPDFException();
        }
        return (ProbabilityFunctionLiteral) parsedStoEx;
    }

}
