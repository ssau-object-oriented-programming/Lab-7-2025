package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function { //класс абстрактный так как не полностью реализован
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }

    public abstract double getFunctionValue(double x);

}
