package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;

    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть > 0 и не равно 1");
        }
        this.base = base;
    }

    public double getBase() {
        return base;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("Аргумент логарифма должен быть больше 0");
        }
        return Math.log(x) / Math.log(base);
    }

    @Override
    public double getLeftDomainBorder() {
        return 0.0; // Логарифм определен для x > 0
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}
