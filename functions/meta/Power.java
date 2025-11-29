package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function func; // исходная функция
    private double power; // степень

    public Power(Function func, double power) throws IllegalArgumentException{
        if(func == null){
            throw new IllegalArgumentException("функция не может быть null"); // проверяем что функция не null
        }
        this.func = func; // устанавливаем исходную функцию
        this.power = power; // устанавливаем степень
    }
    public double getLeftDomainBorder(){
        return func.getLeftDomainBorder(); // возвращаем левую границу исходной функции
    }
    public double getRightDomainBorder(){
        return func.getRightDomainBorder(); // возвращаем правую границу исходной функции
    }
    public double getFunctionValue(double x){
        if(x < func.getLeftDomainBorder() || x > func.getRightDomainBorder()){
            return Double.NaN; // проверяем что x в области определения
        }
        return Math.pow(func.getFunctionValue(x), power); // возводим значение функции в степень
    }
}