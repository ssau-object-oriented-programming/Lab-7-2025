package functions.meta;

import functions.Function;

public class Scale implements Function {
    private final Function functionBase;
    private double scaleX, scaleY;
    public Scale(Function functionBase, double scaleX, double scaleY) throws IllegalArgumentException {
        if (functionBase == null) throw new IllegalArgumentException();
        this.functionBase = functionBase;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    @Override
    public double getLeftDomainBorder() {
        return this.functionBase.getLeftDomainBorder() * this.scaleX;
    }
    @Override
    public double getRightDomainBorder() {
        return this.functionBase.getRightDomainBorder() * this.scaleX;
    }
    @Override
    public double getFunctionValue(double x) {
        return this.functionBase.getFunctionValue(x * this.scaleX) * this.scaleY;
    }
    public double getScaleX() { return this.scaleX; }
    public double getScaleY() { return this.scaleY; }
    public void setScaleX(double scaleX) { this.scaleX = scaleX; }
    public void setScaleY(double scaleY) { this.scaleY = scaleY; }
}
