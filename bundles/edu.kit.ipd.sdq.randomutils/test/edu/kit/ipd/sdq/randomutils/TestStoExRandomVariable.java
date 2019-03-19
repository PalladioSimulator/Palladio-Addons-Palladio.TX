package edu.kit.ipd.sdq.randomutils;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.junit.Test;

public class TestStoExRandomVariable {

    @Test
    public void testConstant() {
        StoExRandomVariable randomVariable = new StoExRandomVariable("DoublePDF[(1.0;0)(1.00000001;1)]");
        double value = randomVariable.next();
        Assert.assertThat(value, equalTo(1.0));
    }

}
