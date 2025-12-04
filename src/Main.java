import functions.*;
import functions.basic.Sin;
import functions.basic.Cos;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Итератор");

        double[] xValues = {0, 1, 2};
        double[] yValues = {0, 1, 4};
        FunctionPoint[] points = new FunctionPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            points[i] = new FunctionPoint(xValues[i], yValues[i]);
        }

        TabulatedFunction f = TabulatedFunctions.createTabulatedFunction(points, points.length);
        for (FunctionPoint p : f) {
            System.out.println(p);
        }

        System.out.println("\nФабрика");
        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        TabulatedFunction tf = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 3);
        System.out.println(tf.getClass().getSimpleName());
        System.out.println(tf);

        System.out.println("\nРефлексия");
        tf = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[]{0, 5, 10});
        System.out.println(tf.getClass().getSimpleName());
        System.out.println(tf);

        tf = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 3);
        System.out.println(tf.getClass().getSimpleName());
        System.out.println(tf);
    }
}