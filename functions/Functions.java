package functions;

import functions.meta.*;

public class Functions {

    // коснтруктор чтобы нельзя было создать объект класса
    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    // вычисление интеграла по методу трапеций
    public static double integrate(Function function, double leftBorder, double rightBorder, double step) {
        // проверка корректности границ интегрирования
        if (leftBorder >= rightBorder) {
            throw new IllegalArgumentException("левая граница интегрирования должна быть меньше правой");
        }

        // проверка шага дискретизации
        if (step <= 0) {
            throw new IllegalArgumentException("шаг дискретизации должен быть положительным");
        }

        // проверка, что интервал интегрирования входит в область определения
        if (leftBorder < function.getLeftDomainBorder() || rightBorder > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("интервал интегрирования выходит за границы области определения функции");
        }

        double integral = 0.0;
        double currentX = leftBorder;

        // проходим по всем полным шагам
        while (currentX + step < rightBorder) {
            double f1 = function.getFunctionValue(currentX);
            double f2 = function.getFunctionValue(currentX + step);
            integral += (f1 + f2) * step / 2.0;
            currentX += step;
        }

        // обрабатываем последний неполный шаг
        if (currentX < rightBorder) {
            double remainingStep = rightBorder - currentX;
            double f1 = function.getFunctionValue(currentX);
            double f2 = function.getFunctionValue(rightBorder);
            integral += (f1 + f2) * remainingStep / 2.0;
        }

        return integral;
    }
}