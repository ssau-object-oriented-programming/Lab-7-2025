package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function fun1;
    private Function fun2;

    public Sum(Function fun1, Function fun2) {
        this.fun1 = fun1;
        this.fun2 = fun2;
    }
    public double getLeftDomainBorder() { //большая левая граница
        if (fun1.getLeftDomainBorder() > fun2.getLeftDomainBorder()) {
            return fun1.getLeftDomainBorder();
        }
        return fun2.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){ //меньшая правая граница
        if (fun1.getRightDomainBorder() < fun2.getRightDomainBorder())
            return fun1.getRightDomainBorder();
        return fun2.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) { //входит ли икс в область определения
            return (fun1.getFunctionValue(x) + fun2.getFunctionValue(x));
        }
        return Double.NaN; //результа не существует
    }
}
