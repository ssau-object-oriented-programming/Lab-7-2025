import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Задание 1 \n");
        TabulatedFunction arrFunc = new ArrayTabulatedFunction(0, 5, 6);
        TabulatedFunction linkFunc = new LinkedListTabulatedFunction(0, 5, 6);
        System.out.println("проверка работы вывода массива \n");
        for (FunctionPoint p : arrFunc) {
            System.out.println(p);
        }
        System.out.println("\nпроверка работы вывода списка \n");
        for (FunctionPoint p : linkFunc) {
            System.out.println(p);
        }

        System.out.println("\nЗадание 2 \n");
        Function f = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.out.println("\nЗадание 3 \n");
        TabulatedFunction func;

        func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(func.getClass());
        System.out.println(func);

    }

}