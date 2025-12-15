package functions.meta;
import static functions.FunctionPoint.largeThan;

import functions.Function;

public class Power implements Function {

    private final Function base;
    private final double degree;
    private final double leftX, rightX;

    public Power(Function base, double degree){
        this.base = base;
        this.degree = degree;

        leftX = base.getLeftDomainBorder();
        rightX = base.getRightDomainBorder();
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
            double a = base.getFunctionValue(x);
            return Math.pow(a, degree);

        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка при возведении в степень: " + e.getMessage());
        }
    }
}
