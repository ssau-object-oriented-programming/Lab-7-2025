package functions;

import functions.meta.*;

public final class Functions {
    private Functions() {
        // Запрет на создание объектов
    }

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

    /**
     * Вычисление интеграла функции методом трапеций
     * @param f функция для интегрирования
     * @param left левая граница интегрирования
     * @param right правая граница интегрирования
     * @param step шаг дискретизации
     */
    public static double integrate(Function f, double left, double right, double step) {
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }
        if (left >= right) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным");
        }

        double integral = 0.0;
        double current = left;

        // Обрабатываем полные шаги
        while (current + step <= right) {
            double f1 = f.getFunctionValue(current);
            double f2 = f.getFunctionValue(current + step);
            integral += (f1 + f2) * step / 2.0;
            current += step;
        }

        // Обрабатываем последний неполный шаг (если есть)
        if (current < right) {
            double lastStep = right - current;
            double f1 = f.getFunctionValue(current);
            double f2 = f.getFunctionValue(right);
            integral += (f1 + f2) * lastStep / 2.0;
        }

        return integral;
    }
}