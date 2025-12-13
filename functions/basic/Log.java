package functions.basic;
import functions.Function;

public class Log implements Function{
    private double base;
    public Log(double base) {
        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0.0; // x > 0, но не включая 0
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN; // Логарифм не определен для x ≤ 0
        }

    // Формула замены основания: logₐ(x) = ln(x) / ln(a)
        return Math.log(x) / Math.log(base);
    }
    


}
