package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function func; // исходная функция
    private double coefx; // коэффициент сдвига по x
    private double coefy; // коэффициент сдвига по y

    public Shift(Function func, double coefx, double coefy) throws IllegalArgumentException{
        if(func == null){
            throw new IllegalArgumentException("функция не может быть null"); // проверяем что функция не null
        }
        this.func = func; // устанавливаем исходную функцию
        this.coefx = coefx; // устанавливаем коэффициент по x
        this.coefy = coefy; // устанавливаем коэффициент по y
    }
    public double getLeftDomainBorder(){
        return func.getLeftDomainBorder()-coefx; // сдвигаем левую границу
    }
    public double getRightDomainBorder(){
        return func.getRightDomainBorder()-coefx; // сдвигаем правую границу
    }
    public double getFunctionValue(double x){
        if(x < getLeftDomainBorder() || x > getRightDomainBorder()){
            return Double.NaN; // проверяем что x в области определения
        }
        return func.getFunctionValue(x+coefx)+coefy; // сдвигаем значение функции
    }
}