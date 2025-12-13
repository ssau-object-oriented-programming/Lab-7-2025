package functions.meta;
import functions.Function;

public class Sum implements Function{
    private Function f1; // Первая функция
    private Function f2;
    
    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        double left1 = f1.getLeftDomainBorder();
        double left2 = f2.getLeftDomainBorder();
        return Math.max(left1, left2);
    }

    public double getRightDomainBorder() {
        double right1 = f1.getRightDomainBorder();
        double right2 = f2.getRightDomainBorder();
        return Math.min(right1, right2);
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        //Проверяем, что каждая функция определена в этой точке
        if (x < f1.getLeftDomainBorder() || x > f1.getRightDomainBorder() ||
            x < f2.getLeftDomainBorder() || x > f2.getRightDomainBorder()) {
            return Double.NaN;
        }
        double value1 = f1.getFunctionValue(x);
        double value2 = f2.getFunctionValue(x);

        return value1 + value2;
    }

}   
