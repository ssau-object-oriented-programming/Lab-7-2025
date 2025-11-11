package functions.meta;

import functions.Function;

public class Scale implements Function {

    private Function fun;
    private double x0;
    private double y0;
    public Scale(Function fun, double x, double y){
        this.fun = fun;
        x0 = x;
        y0 = y;
    }

    public double getLeftDomainBorder() {
        if (x0 > 0) {  //так как коэффициент может быть отрицательным
            return fun.getLeftDomainBorder() * x0;
        }
        return fun.getRightDomainBorder() * x0;
    }

    public double getRightDomainBorder() {
        if (x0 > 0) {
            return fun.getRightDomainBorder() * x0;
        }
        return fun.getLeftDomainBorder() * x0;

    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) { //входит ли икс в область определения
            return (fun.getFunctionValue(x / x0) * y0);  //находим начальное значение функции в точке до масштабирования икс и масштабируем по y
        }
        return Double.NaN;
    }
}
