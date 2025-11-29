package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function func; // исходная функция
    private double coefx; // коэффициент масштабирования по x
    private double coefy; // коэффициент масштабирования по y

    public Scale(Function func, double coefx, double coefy) throws IllegalArgumentException{
        if(func == null){
            throw new IllegalArgumentException("функция не может быть null"); // проверяем что функция не null
        }
        this.func = func; // устанавливаем исходную функцию
        this.coefx = coefx; // устанавливаем коэффициент по x
        this.coefy = coefy; // устанавливаем коэффициент по y
    }
    public double getLeftDomainBorder(){
        if(coefx > 0){
            return func.getLeftDomainBorder()/coefx; // масштабируем левую границу для положительного коэффициента
        }
        else if( coefx < 0){
            return func.getRightDomainBorder()/coefx; // масштабируем правую границу для отрицательного коэффициента
        }
        else{
            return Double.NEGATIVE_INFINITY; // для нулевого коэффициента область определения бесконечна
        }
    }
    public double getRightDomainBorder(){
        if(coefx > 0){
            return func.getRightDomainBorder()/coefx; // масштабируем правую границу для положительного коэффициента
        }
        else if( coefx < 0){
            return func.getLeftDomainBorder()/coefx; // масштабируем левую границу для отрицательного коэффициента
        }
        else{
            return Double.POSITIVE_INFINITY; // для нулевого коэффициента область определения бесконечна
        }
    }
    public double getFunctionValue(double x){
        if(x < getLeftDomainBorder() || x > getRightDomainBorder()){
            return Double.NaN; // проверяем что x в области определения
        }
        return func.getFunctionValue(x*coefx)*coefy; // масштабируем значение функции
    }
}