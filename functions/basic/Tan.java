package functions.basic;
import static functions.FunctionPoint.*;

public class Tan extends TrigonometricFunction {

    public double getFunctionValue(double x) {
        if (equal(Math.cos(x), 0)){
            throw new IllegalArgumentException("Неправильный аргумент для функции тангенс");
        }
        return Math.tan(x);
    }

}
