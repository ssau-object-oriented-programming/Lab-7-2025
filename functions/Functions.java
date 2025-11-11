package functions;

import functions.meta.*;

public class Functions {

    private Functions() {}; //конструктор чтобы нельзя было создать объект класса
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power){
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }
    public static double integral(Function f, double left, double right, double diskr) {
        if (Double.compare(left, f.getLeftDomainBorder()) < 0 || Double.compare(right, f.getRightDomainBorder()) > 0) {
            throw new IllegalArgumentException("Границы для интегрирования выходят за область определения функции");
        }
        if (Double.compare(left, right) >= 0) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        double integralVal = 0;
        double x0;
        double x;
        double count = Math.abs(left - right) / diskr; //число трапеций, которые поместятся в предел интерирования
        for (int i = 0; i < (int) count; i++) { //находим сначала число трапеций с целым шагом дискретизации
            x0 = left + i * diskr;//левая граница для трапеции
            x = left + (i + 1) * diskr; //правая граница для трапеции
            integralVal += ((x - x0) * 0.5 * (f.getFunctionValue(x0) + f.getFunctionValue(x))); //нащли высоту трапеции, отняв от правой границы левую и умножили на полусумму оснований
        }
        if (Double.compare(count, (int) count) != 0) { //если осталась не целая часть шага
            x0 = left + (int) count * diskr;//левая граница для трапеции
            x = right; //правая граница для трапеции равна конечной правой границе
            integralVal += ((x - x0) * 0.5 * (f.getFunctionValue(x0) + f.getFunctionValue(x)));
        }
        return integralVal;
    }
}