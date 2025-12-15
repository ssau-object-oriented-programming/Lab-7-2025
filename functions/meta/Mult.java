package functions.meta;

import functions.Function;

import static functions.FunctionPoint.*;

public class Mult implements Function {

    private final double leftX, rightX;
    private final Function func1, func2;

    public Mult(Function a, Function b) {

        leftX = Math.max(a.getLeftDomainBorder(), b.getLeftDomainBorder());
        rightX = Math.min(a.getRightDomainBorder(), b.getRightDomainBorder());

        if (leftX > rightX) {
            throw new IllegalArgumentException("Функции не имеют общей области определения");
        }

        func1 = a;
        func2 = b;
    }

    public double getLeftDomainBorder() {
        return leftX;
    }

    public double getRightDomainBorder() {
        return rightX;
    }

    public double getFunctionValue(double x) {
        double result;
        if (largeThan(x, rightX) || largeThan(leftX, x)) {
            throw new IllegalArgumentException("Выход за пределы области определения");
        }
        try {
            result = func1.getFunctionValue(x) * func2.getFunctionValue(x);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка при получении значения функций: " + e.getMessage());
        }
        return result;
    }
}
