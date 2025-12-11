package functions.meta;
import functions.Function;

public class Mult implements Function {
    private final Function f1;
    private final Function f2;

    //функция, являющаяся произведением двух других функций: h(x) = f1(x) * f2(x)

    public Mult(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    //Возвращает левую границу области определения, которая является максимумом левых границ исходных функций
    public double getLeftDomainBorder() {
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }

    //Возвращает правую границу области определения, которая является минимумом
    public double getRightDomainBorder() {
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }

    //Возвращает значение функции в заданной точке x как произведение значений исходных функций
    public double getFunctionValue(double x) {
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}