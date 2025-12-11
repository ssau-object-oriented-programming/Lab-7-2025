package functions.meta;

import functions.Function;

public class Scale implements Function {

    private static final double EPS = 1e-9;
    private final Function f;
    private final double scaleX;
    private final double scaleY;

    //Функция, получается из исходной масштабированием: h(x) = scaleY * f(x / scaleX).

    public Scale(Function f, double scaleX, double scaleY) {
        if (Math.abs(scaleX) < EPS) {
            throw new IllegalArgumentException("The scaling factor scaleX cannot be zero (too close to " + EPS + ").");
        }

        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    //Область определения получается масштабированием: [left*scaleX, right*scaleX], при отрицательном scaleX границы меняются местами
    public double getLeftDomainBorder() {
        if (scaleX > 0) {
            return f.getLeftDomainBorder() * scaleX;
        } else {
            return f.getRightDomainBorder() * scaleX;
        }
    }

    public double getRightDomainBorder() {
        if (scaleX > 0) {
            return f.getRightDomainBorder() * scaleX;
        } else {
            return f.getLeftDomainBorder() * scaleX;
        }
    }

     //Возвращает значение функции с масштабированием: scaleY * f(x / scaleX)
     public double getFunctionValue(double x) {
         //Значение аргумента для исходной функции: x / scaleX
         double argument = x / scaleX;

         //Масштабируем значение исходной функции: f(argument) * scaleY
         return f.getFunctionValue(argument) * scaleY;
     }
}