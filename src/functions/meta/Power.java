package functions.meta;

import functions.Function;

import static functions.DoubleComparison.doubleGreater;
import static functions.DoubleComparison.doubleLess;

// Возведение в степень
public class Power implements Function {

    private Function f;
    private double power;

    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (doubleLess(x,getLeftDomainBorder()) || doubleGreater(x,getRightDomainBorder())) {
            return Double.NaN;
        }
        return Math.pow(f.getFunctionValue(x), power);
    }
}
