package functions.basic;

import functions.Function;

//Класс для тригонометрических функций

public abstract class TrigonometricFunction implements Function {

    //Область определения для синуса, косинуса и тангенса: (-бесконечность, +бесконечность)
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}
