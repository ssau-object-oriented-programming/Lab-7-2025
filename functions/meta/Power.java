package functions.meta;

import functions.Function;

public class Power implements Function{
    private Function fun;
    private double deg; //степень

    public Power(Function fun, double deg) {
        this.fun = fun;
        this.deg = deg;
    }
    public double getLeftDomainBorder() {
        return fun.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){
        return fun.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) { //входит ли икс в область определения
            return Math.pow(fun.getFunctionValue(x), deg);
        }
        return Double.NaN; //результа не существует
    }
}
