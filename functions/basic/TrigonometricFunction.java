package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {

    public double getLeftDomainBorder(){
        return Double.NEGATIVE_INFINITY; // тригонометрические функции определены на всей числовой прямой
    }
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY; // тригонометрические функции определены на всей числовой прямой
    }
    public abstract double getFunctionValue(double x); // абстрактный метод для вычисления значения функции
}