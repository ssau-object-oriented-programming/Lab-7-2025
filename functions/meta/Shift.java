package functions.meta;

import static functions.FunctionPoint.largeThan;

import functions.Function;

public class Shift implements Function{

    private final double leftX, rightX;

    private final Function func;

    private final double shift_x; 
    private final double shift_y;

    public Shift(Function func, double shift_x, double shift_y){

        this.shift_x = shift_x;
        this.shift_y = shift_y;


        this.leftX = func.getLeftDomainBorder() + shift_x;
        this.rightX = func.getRightDomainBorder() + shift_x;

        this.func = func;
    }

    public double getLeftDomainBorder(){
        return leftX;
    }

    public double getRightDomainBorder(){
        return rightX;
    }

    public double getFunctionValue(double x){
        if (largeThan(x, rightX) || largeThan(leftX, x)){
            throw new IllegalArgumentException("Выход за область определения");
        }
        try{
            return func.getFunctionValue(x - shift_x) + shift_y;
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка при вычислении значения функции: " + e.getMessage());
        }
    }
}
