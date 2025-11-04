package functions.basic;
import functions.Function;

public class Log implements Function {
    private static final double E = 1e-10;
    private double a;

    public Log(double a) {
        this.a = a;
    }

    public double getLeftDomainBorder() {
        return 0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x < -E)
            return Double.NaN;
        return Math.log(x) / Math.log(a);
    }
}