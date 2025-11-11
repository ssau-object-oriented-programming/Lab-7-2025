package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function fun1;
    private Function fun2;

    public Composition(Function fun1, Function fun2) {
        this.fun1 = fun1;
        this.fun2 = fun2;
    }
    public double getLeftDomainBorder() {
        return fun2.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return fun2.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) { //входит ли икс в область определения
            return fun1.getFunctionValue(fun2.getFunctionValue(x));
        }
        return Double.NaN; //результа не существует
    }
}
