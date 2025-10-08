package functions.meta;

import functions.Function;

public class Power implements Function {
    private final Function functionBase;
    private double power;
    public Power(Function functionBase, double power) throws IllegalArgumentException {
        if (functionBase == null) throw new IllegalArgumentException();
        this.functionBase = functionBase;
        this.power = power;
    }
    @Override
    public double getLeftDomainBorder() {
        return functionBase.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return functionBase.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        return Math.pow(functionBase.getFunctionValue(x), power);
    }
    public void setPower(double power) { this.power = power; }
    public double getPower() { return this.power; }
}
