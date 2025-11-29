package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function first; // первая функция для сложения
    private Function second; // вторая функция для сложения
    public Sum(Function first, Function second) throws IllegalArgumentException{
        if(first == null || second == null){
            throw new IllegalArgumentException("функции не могут быть null"); // проверяем что функции не null
        }
        this.first = first; // устанавливаем первую функцию
        this.second = second; // устанавливаем вторую функцию
    }

    public double getLeftDomainBorder(){
        return Math.max(first.getLeftDomainBorder(), second.getLeftDomainBorder()); // возвращаем пересечение левых границ
    }
    public double getRightDomainBorder(){
        return Math.min(first.getRightDomainBorder(), second.getRightDomainBorder()); // возвращаем пересечение правых границ
    }
    public double getFunctionValue(double x){
        if(x < getLeftDomainBorder() || x > getRightDomainBorder()){
            return Double.NaN; // проверяем что x в области определения
        }
        return first.getFunctionValue(x)+second.getFunctionValue(x); // вычисляем сумму функций
    }
}