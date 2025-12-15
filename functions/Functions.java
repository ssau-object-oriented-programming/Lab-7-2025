package functions;

import functions.meta.*;

public final class Functions {

    private Functions() {}

    // Возвращает функцию, полученную из исходной сдвигом вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    // Возвращает функцию, полученную из исходной масштабированием вдоль осей
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    // Возвращает функцию, являющуюся заданной степенью исходной
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    // Возвращает функцию, являющуюся суммой двух исходных функций
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    // Возвращает функцию, являющуюся произведением двух исходных функций
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    // Возвращает функцию, являющуюся композицией двух исходных функций
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }



    public static double integrate(Function function, double leftBorder, double rightBorder, double step) {
        // Проверка границ области определения
        if (leftBorder < function.getLeftDomainBorder() || rightBorder > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    "Интервал интегрирования [" + leftBorder + ", " + rightBorder + "] " +
                            "выходит за область определения функции [" +
                            function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]"
            );
        }

        // Проверка корректности параметров
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным: " + step);
        }

        if (leftBorder >= rightBorder) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftBorder + " >= " + rightBorder);
        }

        double integral = 0.0;
        double currentX = leftBorder;

        // Проходим по всем полным шагам
        while (currentX + step <= rightBorder) {
            double f1 = function.getFunctionValue(currentX);
            double f2 = function.getFunctionValue(currentX + step);
            integral += (f1 + f2) * step / 2.0;
            currentX += step;
        }

        // Обрабатываем последний неполный шаг (если есть)
        if (currentX < rightBorder) {
            double remainingStep = rightBorder - currentX;
            double f1 = function.getFunctionValue(currentX);
            double f2 = function.getFunctionValue(rightBorder);
            integral += (f1 + f2) * remainingStep / 2.0;
        }

        return integral;
    }
}


