package functions.basic;

public class Tan extends TrigonometricFunction {

    public double getFunctionValue(double x) {
        double cos = Math.cos(x);

        //если косинус близок к нулю
        if (Math.abs(cos) < 1e-10) {
            return Double.NaN;
        }
        return Math.tan(x);
    }
}
