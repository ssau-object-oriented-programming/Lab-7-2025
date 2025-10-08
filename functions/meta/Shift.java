package functions.meta;

import functions.Function;

public class Shift implements Function {
    private final Function functionBase;
    private double offsetX, offsetY;
    public Shift(Function functionBase, double offsetX, double offsetY) throws IllegalArgumentException {
        if (functionBase == null) throw new IllegalArgumentException();
        this.functionBase = functionBase;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    @Override
    public double getLeftDomainBorder() {
        return this.functionBase.getLeftDomainBorder() + this.offsetX;
    }
    @Override
    public double getRightDomainBorder() {
        return this.functionBase.getRightDomainBorder() + this.offsetX;
    }
    @Override
    public double getFunctionValue(double x) {
        return this.functionBase.getFunctionValue(x + this.offsetX) + this.offsetY;
    }
    public double getOffsetX() { return this.offsetX; }
    public double getOffsetY() { return this.offsetY; }
    public void setOffsetX(double offsetX) { this.offsetX = offsetX; }
    public void setOffsetY(double offsetY) { this.offsetY = offsetY; }
}
