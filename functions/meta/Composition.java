package functions.meta;

import static functions.FunctionPoint.largeThan;

import functions.Function;

public class Composition implements Function{

    private final Function func, func_arg;

    private final double leftX, rightX;


    public Composition(Function func, Function func_arg){

        this.func = func;
        this.func_arg = func_arg;

        this.leftX = func_arg.getLeftDomainBorder();
        this.rightX = func_arg.getRightDomainBorder();
        


    }

    public double getLeftDomainBorder(){
        return leftX;
    }

    public double getRightDomainBorder(){
        return rightX;
    }

    // Функция для обработки g(x)
    private double getFunctionArgValue(double x){

        // Нужно проверить область определения g(x)
        if (largeThan(x, rightX) || largeThan(leftX, x)) {
            throw new IllegalArgumentException("Выход за область определения");
        }

        // Нужно получить значение функции g(x)
        try{
            return func_arg.getFunctionValue(x);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка при получении значения аргумента композиции: " + e.getMessage());
        }

    }


    public double getFunctionValue(double x){
        double arg;
        try{
            arg = getFunctionArgValue(x);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка при получении значения аргумента: " + e.getMessage());
        }

        if (largeThan(arg, func.getRightDomainBorder()) || largeThan(func.getLeftDomainBorder(), arg)){
            throw new IllegalArgumentException("Выход за область определения функции");
        }

        try{    
            return func.getFunctionValue(arg);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка при вычичслении значения функции: " + e.getMessage());
        }
    }
}
