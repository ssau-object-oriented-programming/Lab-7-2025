package functions.meta;
import static functions.FunctionPoint.*;
import functions.Function;

public class Scale implements Function { // класс масштабированной функции

    private final double leftX, rightX;
    private final Function func;
    private final double ratio_x, ratio_y;
    public Scale(Function func, double ratio_x, double ratio_y){

        if (equal(ratio_x, 0) || equal(ratio_y, 0)){
            throw new IllegalArgumentException("Коэффициенты растяжения не могут быть равны 0!");
        }
        this.ratio_x = ratio_x;
        this.ratio_y = ratio_y;
        if (ratio_x > 0){
        this.leftX = ratio_x * func.getLeftDomainBorder();
        this.rightX = ratio_x * func.getRightDomainBorder();
        }else{
            this.leftX = ratio_x * func.getRightDomainBorder();
            this.rightX = ratio_x * func.getLeftDomainBorder();
        }
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
            return ratio_y*func.getFunctionValue(x / ratio_x);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Ошибка в вычислении значения функции: " + e.getMessage());
        }
    }
}
