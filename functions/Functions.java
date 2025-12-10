package functions;

import functions.meta.*;

public final class Functions {
    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY);
    }
    
    public static Function power(Function f, double power){
        return new Power(f, power); 
    }

    public static Function sum(Function f1, Function f2){
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }

    public static double integrate(Function function, double leftX, double rightX, double step) {
        if (Math.abs(leftX - function.getLeftDomainBorder()) < 1e-9 || Math.abs(rightX - function.getRightDomainBorder()) < 1e-9) {
            throw new IllegalArgumentException("Интервал интегрирования [" + leftX + ", " + rightX + "] выходит за границы области определения функции ");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным.");
        }
        if (leftX > rightX) {
            return -integrate(function, rightX, leftX, step);
        }
        if (leftX == rightX) {
            return 0;
        }

        double integralSum = 0.0;
        double currentX = leftX;

        while (currentX < rightX) {
            step = Math.min(step, rightX - currentX);
            double x1 = currentX;
            double x2 = currentX + step;
            double y1 = function.getFunctionValue(x1);
            double y2 = function.getFunctionValue(x2);
            integralSum += (y1 + y2) / 2.0 * step;
            currentX += step;
        }
        return integralSum;
    }
}
