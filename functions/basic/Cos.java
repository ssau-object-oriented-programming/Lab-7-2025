package functions.basic;

public class Cos extends TrigonometricFunction{
    public Cos() {
        super();
    }

    public double getFunctionValue(double x) {
        return Math.cos(x);
    }

    public String toString() {
        return "cos(x)";
    }
    
}
