package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base; // основание логарифма

    // конструктор принимает основание логарифма
    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1"); // проверяем валидность основания
        }
        this.base = base; // устанавливаем основание логарифма
    }

    // вычисляем значение логарифма в точке x
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN; // логарифм определен только для положительных чисел
        }
        return Math.log(x) / Math.log(base); // вычисляем логарифм по формуле перехода к новому основанию
    }

    // получаем левую границу области определения
    public double getLeftDomainBorder() {
        return 0; // логарифм определен от 0 не включая
    }

    // получаем правую границу области определения
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY; // логарифм определен до бесконечности
    }
}