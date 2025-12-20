package functions.meta;

import functions.Function;

import static functions.DoubleComparison.doubleGreater;
import static functions.DoubleComparison.doubleLess;

//композиция двух исходных функций
public class Composition implements Function {

    private Function f1; //внешняя функция
    private Function f2; //внутренняя функция

    public Composition(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    //Область определения совпадает с областью определения внутренней функции
    public double getLeftDomainBorder() {
        return f2.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return f2.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {

        if (doubleLess(x,getLeftDomainBorder()) || doubleGreater(x,getRightDomainBorder())) {
            return Double.NaN;
        }
        double innerValue = f2.getFunctionValue(x);

        //результат внутренней функции в области определения внешней
        if (doubleLess(innerValue, f1.getLeftDomainBorder())
                || doubleGreater(innerValue,f1.getRightDomainBorder())) {
            return Double.NaN;
        }

        return f1.getFunctionValue(innerValue);
    }
}