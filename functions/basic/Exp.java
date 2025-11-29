package functions.basic;

import functions.Function;

public class Exp implements Function{

    public double getLeftDomainBorder(){
        return Double.NEGATIVE_INFINITY; // возвращаем левую границу области определения
    }
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY; // возвращаем правую границу области определения
    }
    public double getFunctionValue(double x){
        return Math.exp(x); // вычисляем значение экспоненты в точке x
    }
}