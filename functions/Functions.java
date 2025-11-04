package functions;
import functions.meta.*;

public class Functions {
    private static final double E = 1e-10;

    public Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double n) {
        return new Power(f, n);
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

    public static double integrate(Function f, double leftX, double rightX, double step) {
        if (leftX < f.getLeftDomainBorder() + E || rightX > f.getRightDomainBorder() - E)
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        if (step <= 0)
            throw new IllegalArgumentException("Шаг отрицателен или равен 0");
        double integral = 0;
        if (leftX <= rightX + E) {
            double y1 = f.getFunctionValue(leftX);
            double x2 = leftX + step;
            for (double y2 = f.getFunctionValue(x2); x2 <= rightX + E; leftX = x2, x2 += step, y1 = y2, y2 = f.getFunctionValue(x2))
                integral += (y1 + y2) * step / 2;
            if (leftX < rightX + E)
                integral += (y1 + f.getFunctionValue(rightX)) * (rightX - leftX) / 2;
        }
        else {
            double y1 = f.getFunctionValue(rightX);
            double x2 = rightX + step;
            for (double y2 = f.getFunctionValue(x2); x2 <= leftX + E; rightX = x2, x2 += step, y1 = y2, y2 = f.getFunctionValue(x2))
                integral -= (y1 + y2) * step / 2;
            if (rightX < leftX + E)
                integral -= (y1 + f.getFunctionValue(leftX)) * (leftX - rightX) / 2;
        }
        return integral;
    }
}