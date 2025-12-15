package functions;

import functions.meta.Composition;
import functions.meta.Mult;
import functions.meta.Power;
import functions.meta.Scale;
import functions.meta.Shift;
import functions.meta.Sum;

import static functions.FunctionPoint.largeThan;
import static functions.FunctionPoint.equal;

public final class Functions {

    private Functions(){}

    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power){
        return new Power(f, power); // Переписать код
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

public static double integration(Function function, double leftX, double rightX, double step) {
        // Проверка корректности параметров
        if (equal(step, 0) || largeThan(0, step)) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }
        if (largeThan(leftX, rightX) || equal(rightX, leftX)) {
            throw new IllegalArgumentException("Правая граница должна быть больше левой");
        }
        // Проверка области определения
        if (largeThan(function.getLeftDomainBorder(), leftX)  || largeThan(rightX, function.getRightDomainBorder())) {
            throw new IllegalArgumentException(
                String.format("Интервал интегрирования [%.3f, %.3f] выходит за область определения [%.3f, %.3f]",
                    leftX, rightX, function.getLeftDomainBorder(), function.getRightDomainBorder())
            );
        }
        double integral = 0.0;
        double currentX = leftX;
        // Первая точка (левая граница)
        double prevY = function.getFunctionValue(currentX);
        // Цикл по всем сегментам
        while (currentX < rightX) {
            double nextX = Math.min(currentX + step, rightX); // Последний сегмент может быть короче
            double nextY = function.getFunctionValue(nextX);
            // Площадь трапеции: (основание) * (средняя высота)
            double segmentArea = (nextX - currentX) * (prevY + nextY) / 2.0;
            integral += segmentArea;
            // Переходим к следующему сегменту
            currentX = nextX;
            prevY = nextY;
        }
        return integral;
    }

}
