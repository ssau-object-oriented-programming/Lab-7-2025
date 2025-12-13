package functions.basic;

public class Tan extends TrigonometricFunction {
    public Tan() {
        super();
    }

    public double getFunctionValue(double x) {
        // Тангенс не определен, когда cos(x) = 0
        // Это происходит в точках π/2 + πk, где k - целое число
        double cosValue = Math.cos(x);
        if (Math.abs(cosValue) < 1e-12) {
            return Double.NaN;
        }
        return Math.tan(x);
    }

    public String toString() {
        return "tan(x)";
    }
    
}
