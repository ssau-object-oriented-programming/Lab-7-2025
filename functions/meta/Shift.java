package functions.meta;

import functions.Function;

public class Shift implements Function{
    private Function fun;
    private double x0;
    private double y0;
    public Shift(Function fun, double x, double y){
        this.fun = fun;
        x0 = x;
        y0 = y;
    }

    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder() + x0;
    }

    public double getRightDomainBorder() {
        return fun.getRightDomainBorder() + x0;
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) { //входит ли икс в область определения
            return (fun.getFunctionValue(x - x0) + y0);
        }
        return Double.NaN;
    }
}
