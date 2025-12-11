package functions.basic;

import functions.Function;

//логарифм по заданному основанию: f(x) = log_base(x)
public class Log implements Function {
    private final double base; // Основание логарифма

    // Конструктор, принимающий основание логарифма
    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("The base of the logarithm must be > 0 and not equal to 1");
        }
        this.base = base;
    }

    //Область определения: (0, +бесконечность)
    public double getLeftDomainBorder() {
        // Логарифм не определен в нуле, поэтому используем малое положительное число 0.0
        return 0.0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }


    //Вычисляет значение логарифма по формуле смены основания: log_a(x) = ln(x) / ln(a).
    public double getFunctionValue(double x) {
        //Если x не в области определения (x <= 0), Math.log(x) вернет NaN
        if (x <= 0) {
            return Double.NaN;
        }
        return Math.log(x) / Math.log(base);
    }
}