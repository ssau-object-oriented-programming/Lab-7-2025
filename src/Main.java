import functions.*;
import threads.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try {
            demoLab7();
        } catch (Exception e) {
            System.out.println("Ошибка демонстрации lab5: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demoLab7() {
        System.out.println("\n=== Демонстрация лабораторной работы №7 ===");

        Function f = new functions.basic.Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        System.out.println("Iterating:");
        for (FunctionPoint p : tf) {
            System.out.println(p);
        }

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunction t1 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(t1.getClass());
        System.out.println(t1);

        TabulatedFunction t2 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(t2.getClass());
        System.out.println(t2);

        TabulatedFunction t3 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {
            new FunctionPoint(0, 0), new FunctionPoint(10, 10)
        });
        System.out.println(t3.getClass());
        System.out.println(t3);

        TabulatedFunction t4 = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new functions.basic.Sin(), 0, Math.PI, 11);
        System.out.println(t4.getClass());
        System.out.println(t4);
    }

    private static void nonThread() {
        System.out.println("\n--- nonThread() ---");
        Task task = new Task();
        task.setCount(100);
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < task.getCount(); i++) {
            double base = 1.0 + rnd.nextDouble() * 9.0;
            Function f = new functions.basic.Log(base);
            double left = rnd.nextDouble() * 100.0;
            double right = 100.0 + rnd.nextDouble() * 100.0;
            double step = rnd.nextDouble();
            task.putNoSync(f, left, right, step);
            System.out.printf("Source %.6f %.6f %.6f\n", left, right, step);
            double res;
            try {
                res = Functions.integrate(f, left, right, step <= 0 ? 1e-3 : step);
            } catch (IllegalArgumentException ex) {
                res = Double.NaN;
            }
            System.out.printf("Result %.6f %.6f %.6f %.6f\n", left, right, step, res);
        }
    }

}