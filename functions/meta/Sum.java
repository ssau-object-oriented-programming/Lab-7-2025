package functions.meta;

import functions.Function;
public class Sum implements Function {
    private final Function function1;
    private final Function function2;
    public Sum(Function function1, Function function2) throws IllegalArgumentException {
        if (function1 == null || function2 == null) throw new IllegalArgumentException();
        double lX1, lX2, rX1, rX2;
        lX1 = function1.getLeftDomainBorder();
        lX2 = function2.getLeftDomainBorder();
        rX1 = function1.getRightDomainBorder();
        rX2 = function2.getRightDomainBorder();
        if (rX1 < lX2 || rX2 < lX1) throw new IllegalArgumentException("The intersection of the definition area was not found");
        this.function1 = function1;
        this.function2 = function2;
    }
    @Override
    public double getLeftDomainBorder() {
        return Math.max(function1.getLeftDomainBorder(), function2.getLeftDomainBorder());
    }
    @Override
    public double getRightDomainBorder() {
        return Math.min(function1.getRightDomainBorder(), function2.getRightDomainBorder());
    }
    @Override
    public double getFunctionValue(double x) {
        return function1.getFunctionValue(x) + function2.getFunctionValue(x);
    }
}
