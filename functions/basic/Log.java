package functions.basic;

import functions.Function;

public class Log implements Function {
    private double a; //основание логарифма
    public Log(double a) {
        this.a = a;
    }

    public double getLeftDomainBorder() {
        return Double.MIN_EXPONENT; //минимальный показатель степени, который может иметь переменная double
    }

    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x){
        if (x <= 0) {
            return Double.NaN;
        }
        return  Math.log(x) / Math.log(a);
    }
}