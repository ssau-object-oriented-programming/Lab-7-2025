package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function first; // первая функция для композиции
    private Function second; // вторая функция для композиции

    public Composition(Function first, Function second) throws IllegalArgumentException{
        if(first == null || second == null){
            throw new IllegalArgumentException("Функции не могут быть null"); // проверяем что функции не null
        }
        this.first = first; // устанавливаем первую функцию
        this.second = second; // устанавливаем вторую функцию
    }
    public double getLeftDomainBorder(){
        return first.getLeftDomainBorder(); // возвращаем левую границу первой функции
    }
    public double getRightDomainBorder(){
        return second.getRightDomainBorder(); // возвращаем правую границу второй функции
    }
    public double getFunctionValue(double x){
        if(x < getLeftDomainBorder() || x> getRightDomainBorder()){
            return Double.NaN; // проверяем что x в области определения
        }
        return first.getFunctionValue(second.getFunctionValue(x)); // вычисляем композицию функций
    }
}