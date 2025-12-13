package functions;
import functions.meta.*;

public class Functions {
    
    private Functions() {
        throw new UnsupportedOperationException("Класс Functions не может быть инстанцирован");
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

    public static double integral(Function f, double a, double b, double h) {
        if (h <= 0) throw new IllegalArgumentException("h <= 0");
        if (a >= b) throw new IllegalArgumentException("a >= b");
        if (a < f.getLeftDomainBorder() || b > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал вне области определения");
        }
        
        double sum = 0;
        double x = a;
        
        while (x < b) {
            double xNext = Math.min(x + h, b);
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(xNext);
            sum += (y1 + y2) * (xNext - x) / 2;
            x = xNext;
        }
        
        return sum;
    }
}
