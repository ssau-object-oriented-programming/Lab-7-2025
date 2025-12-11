package functions.meta;

import functions.Function;

public class Shift implements Function {
    private final Function f;
    private final double shiftX;
    private final double shiftY;

    //Функция, полученная из исходной сдвигом: h(x) = f(x - shiftX) + shiftY
    public Shift(Function f, double shiftX, double shiftY) {
        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    //Область определения сдвигается вдоль оси абсцисс: [left + shiftX, right + shiftX]
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + shiftX;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + shiftX;
    }

    //Возвращает значение функции со сдвигом: f(x - shiftX) + shiftY.
    public double getFunctionValue(double x) {
        //Значение аргумента для исходной функции
        double argument = x - shiftX;

        //Сдвигаем значение исходной функции
        return f.getFunctionValue(argument) + shiftY;
    }
}