package functions.meta;

import functions.Function;

import static functions.DoubleComparison.doubleGreater;
import static functions.DoubleComparison.doubleLess;

// Масштабирование вдоль осей координат
public class Scale implements Function {

    private Function f;
    private double scaleX; //коэффициент масштабирования по X
    private double scaleY; //коэффициент масштабирования по Y

    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() * scaleX;
    }
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() * scaleX;
    }

    public double getFunctionValue(double x) {
        double scaledX = x / scaleX;

        if (doubleLess(scaledX, f.getLeftDomainBorder()) || doubleGreater(scaledX, f.getRightDomainBorder())) {
            return Double.NaN;
        }

        return scaleY * f.getFunctionValue(scaledX);
    }

}
