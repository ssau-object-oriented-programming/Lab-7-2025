package functions.basic;

import functions.Function;

public class Exp implements Function{
    public Exp() {
    } // пустой конструктор, так как функция не имеет параметров
    
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
}
