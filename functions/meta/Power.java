package functions.meta;
import functions.Function;

public class Power implements Function {
    private final Function base;
    private final double power;

     //Функция, являющаяся степенью другой функции: h(x) = base(x)^power
    public Power(Function base, double power) {
        this.base = base;
        this.power = power;
    }

    //Область определения совпадает с областью определения базовой функции
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return base.getRightDomainBorder();
    }

    //Возвращает значение функции в заданной точке x
    public double getFunctionValue(double x) {
        return Math.pow(base.getFunctionValue(x), power);
    }
}