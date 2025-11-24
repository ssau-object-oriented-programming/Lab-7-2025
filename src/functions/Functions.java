package functions;

import functions.meta.Composition;
import functions.meta.Mult;
import functions.meta.Power;
import functions.meta.Scale;
import functions.meta.Shift;
import functions.meta.Sum;

public final class Functions {
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

    public static Function composition(Function outer, Function inner) {
        return new Composition(outer, inner);
    }

    public static double integrate(Function f, double left, double right, double step) {
        if (f == null) throw new IllegalArgumentException("Function is null");
        if (Double.isNaN(left) || Double.isNaN(right) || Double.isNaN(step))
            throw new IllegalArgumentException("Arguments must be numbers");
        if (step <= 0) throw new IllegalArgumentException("Step must be positive");

        double domainLeft = f.getLeftDomainBorder();
        double domainRight = f.getRightDomainBorder();
        if (left < domainLeft || right > domainRight) {
            throw new IllegalArgumentException("Integration interval is outside function domain");
        }

        double a = Math.min(left, right);
        double b = Math.max(left, right);

        double sum = 0.0;
        double x = a;
        double fx = f.getFunctionValue(x);

        if (Double.isNaN(fx)) throw new IllegalArgumentException("Function undefined at left border");

        while (x + step < b) {
            double xNext = x + step;
            double fxNext = f.getFunctionValue(xNext);
            if (Double.isNaN(fxNext)) throw new IllegalArgumentException("Function undefined inside interval");
            sum += 0.5 * (fx + fxNext) * (xNext - x);
            x = xNext;
            fx = fxNext;
        }

        if (x < b) {
            double xNext = b;
            double fxNext = f.getFunctionValue(xNext);
            if (Double.isNaN(fxNext)) throw new IllegalArgumentException("Function undefined at right border");
            sum += 0.5 * (fx + fxNext) * (xNext - x);
        }
        
        return left <= right ? sum : -sum;
    }
}
