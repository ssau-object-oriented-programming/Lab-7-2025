package functions.meta;

import functions.Function;

import static functions.DoubleComparison.doubleGreater;
import static functions.DoubleComparison.doubleLess;

//сдвиг вдоль осей координат
public class Shift implements Function {

    private Function f;
    private double shiftX; //сдвиг по X
    private double shiftY; //сдвиг по Y


    public Shift(Function f, double shiftX, double shiftY) {
        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + shiftX;
    }
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + shiftX;
    }

    public double getFunctionValue(double x) {
        double shiftedX = x - shiftX;

        if (doubleLess(shiftedX, f.getLeftDomainBorder()) || doubleGreater(shiftedX, f.getRightDomainBorder())) {
            return Double.NaN;
        }
        return f.getFunctionValue(shiftedX) + shiftY;
    }
}
