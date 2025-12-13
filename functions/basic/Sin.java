package functions.basic;

public class Sin extends TrigonometricFunction {
    public Sin(){
        super();
    }

    public double getFunctionValue(double x) {
        return Math.sin(x);
    }

    public String toString() {
        return "sin(x)";
    }
}
