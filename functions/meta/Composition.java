package functions.meta;

import functions.Function;

public class Composition implements Function {
    private final Function functionOuter;
    private final Function functionInner;
    public Composition(Function functionOuter, Function functionInner) throws IllegalArgumentException {
        if (functionOuter == null || functionInner == null) throw new IllegalArgumentException();
        this.functionOuter = functionOuter;
        this.functionInner = functionInner;
    }
    @Override
    public double getLeftDomainBorder() {
        return functionInner.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return functionInner.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        return functionOuter.getFunctionValue(functionInner.getFunctionValue(x));
    }
}