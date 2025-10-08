package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;
    public Log(double base) {
        this.base = base;
    }
    @Override
    public double getLeftDomainBorder() {
        return 0;
    }
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double getFunctionValue(double x) {
        return Math.log(x) / Math.log(base); // используем формулу изменения основания
    }
    public void setBase(double base) { this.base = base; }
    public double getBase() { return this.base; }
}
