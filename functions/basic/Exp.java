package functions.basic;

import functions.Function;

// функция экспоненты: f(x) = e^x
public class Exp implements Function {

    //Область определения: (-бесконечность, +бесконечность)

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

     //Вычисляет значение e^x

    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
}
