package edu.kit.ipd.sdq.randomutils;

import org.palladiosimulator.commons.stoex.api.StoExParser.SyntaxErrorException;

import de.uka.ipd.sdq.probfunction.ProbabilityDensityFunction;
import de.uka.ipd.sdq.probfunction.ProbabilityMassFunction;
import de.uka.ipd.sdq.probfunction.math.ManagedPDF;
import de.uka.ipd.sdq.stoex.Expression;
import de.uka.ipd.sdq.stoex.ProbabilityFunctionLiteral;
import edu.kit.ipd.sdq.randomutils.internal.Activator;

public final class ManagedPDFParser {

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
            parsedStoEx = Activator.getInstance()
                .getStoexParser()
                .parse(s);
        } catch (SyntaxErrorException e) {
            throw new StringNotPDFException(e.getMessage());
        }
        
        if (!(parsedStoEx instanceof ProbabilityFunctionLiteral)) {
            throw new StringNotPDFException();
        }
        return (ProbabilityFunctionLiteral) parsedStoEx;
    }
    
}
