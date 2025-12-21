import functions.ArrayTabulatedFunction;
import functions.Function;
import functions.FunctionPoint;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.basic.Cos;
import functions.basic.Sin;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Iterator check ===");
        iteratorDemo();

        System.out.println("\n=== Factory method check ===");
        factoryDemo();

        System.out.println("\n=== Reflection creation check ===");
        reflectionDemo();
    }

    private static void iteratorDemo() {
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(new FunctionPoint[] {
                new FunctionPoint(0.0, 0.0),
                new FunctionPoint(1.0, 1.0),
                new FunctionPoint(2.0, 4.0)
        });

        TabulatedFunction listFunction = new LinkedListTabulatedFunction(new FunctionPoint[] {
                new FunctionPoint(-1.0, 1.0),
                new FunctionPoint(0.0, 0.0),
                new FunctionPoint(1.0, 1.0),
                new FunctionPoint(2.0, 4.0)
        });

        System.out.println("ArrayTabulatedFunction iteration:");
        for (FunctionPoint point : arrayFunction) {
            System.out.println(point);
        }

        System.out.println("LinkedListTabulatedFunction iteration:");
        for (FunctionPoint point : listFunction) {
            System.out.println(point);
        }
    }

    private static void factoryDemo() {
        Function cos = new Cos();
        TabulatedFunction tf = TabulatedFunctions.tabulate(cos, 0.0, Math.PI, 11);
        System.out.println("Default factory produced: " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0.0, Math.PI, 11);
        System.out.println("Switched factory produced: " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
    }

    private static void reflectionDemo() {
        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0.0, 10.0, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0.0, 10.0, new double[] {0.0, 10.0});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0.0, 0.0),
                        new FunctionPoint(10.0, 10.0)
                }
        );
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0.0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
    }
}
