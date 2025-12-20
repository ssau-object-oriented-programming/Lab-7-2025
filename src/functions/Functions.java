package functions;
import functions.meta.*;
import static functions.DoubleComparison.*;

public class Functions {
    private Functions() {}

    //возвращает объект функции, полученной из исходной сдвигом вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    //возвращает объект функции, полученной из исходной масштабированием вдоль осей
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    //возвращает объект функции, являющейся заданной степенью исходной
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    //возвращает объект функции, являющейся суммой двух исходных
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    //возвращает объект функции, являющейся произведением двух исходных
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    //возвращает объект функции, являющейся композицией двух исходных
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    //вычисляет значение интеграла функции методом трапеций
    public static double integral(Function f, double leftX, double rightX, double step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }

        if (doubleLess(leftX, f.getLeftDomainBorder()) || doubleGreater(rightX, f.getRightDomainBorder())) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за область определения функции");
        }

        if (doubleGreaterOrEq(leftX, rightX)) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double sum = 0.0;
        double currentX = leftX;

        while (doubleLess(currentX, rightX)) {

            double nextX = Math.min(currentX + step, rightX);

            double y1 = f.getFunctionValue(currentX);
            double y2 = f.getFunctionValue(nextX);

            if (Double.isNaN(y1) || Double.isNaN(y2)) {
                return Double.NaN;
            }

            double trapezoidS = (y1 + y2) * (nextX - currentX) / 2.0;
            sum += trapezoidS;

            currentX = nextX;
        }
        return sum;
    }
}
