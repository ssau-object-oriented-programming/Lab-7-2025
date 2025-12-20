package functions;

import functions.meta.*;

public final class Functions {

    private Functions() {
        throw new AssertionError("Нельзя создать экземпляр класса Functions");
    }
    public static Function shift(Function f, double shiftX, double shiftY) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        if (scaleX == 0) {
            throw new IllegalArgumentException("Коэффициент масштабирования по X не может быть 0");
        }
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Composition(f1, f2);
    }
    public static Function difference(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Sum(f1, new Scale(f2, 1, -1));
    }
    public static Function division(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Mult(f1, new Power(f2, -1));
    }

    public static double integrate(Function function, double leftX, double rightX, double step) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }

        // Проверка границ интегрирования
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    String.format("Интервал интегрирования [%.4f, %.4f] выходит за границы области определения функции [%.4f, %.4f]",
                            leftX, rightX, function.getLeftDomainBorder(), function.getRightDomainBorder())
            );
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница интегрирования должна быть меньше правой: " +
                    leftX + " >= " + rightX);
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным: " + step);
        }

        // Метод трапеций
        double integral = 0.0;
        double currentX = leftX;

        // Проходим по всей области интегрирования
        while (currentX < rightX) {
            double nextX = Math.min(currentX + step, rightX); // Последний шаг может быть меньше

            double fCurrent = function.getFunctionValue(currentX);
            double fNext = function.getFunctionValue(nextX);

            // Проверка на NaN (функция не определена)
            if (Double.isNaN(fCurrent) || Double.isNaN(fNext)) {
                throw new IllegalArgumentException(
                        String.format("Функция не определена в точке x=%.4f или x=%.4f", currentX, nextX)
                );
            }

            // Площадь трапеции: (a + b) * h / 2
            double trapezoidArea = (fCurrent + fNext) * (nextX - currentX) / 2.0;
            integral += trapezoidArea;

            currentX = nextX;
        }

        return integral;
    }
}
