package functions.meta;

import functions.Function;

public class Composition implements Function {
    private final Function f1; // Внешняя функция
    private final Function f2; // Внутренняя функция

    //Композиция двух функций: h(x) = f1(f2(x))
    public Composition(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    //Область определения считаем совпадающей с областью определения внутренней функции f2
    public double getLeftDomainBorder() {
        return f2.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return f2.getRightDomainBorder();
    }

    //Возвращает значение композиции: f1(f2(x)).
    public double getFunctionValue(double x) {
        // Сначала вычисляем значение внутренней функции f2
        double innerValue = f2.getFunctionValue(x);

        //Используем это значение как аргумент для внешней функции f1
        return f1.getFunctionValue(innerValue);
    }
}