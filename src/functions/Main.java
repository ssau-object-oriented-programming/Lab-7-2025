package functions;

import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Sin;

public class Main {

    public static void main(String[] args) {

        System.out.println("Проверка интегрирования экспоненты на [0,1]");
        Function f = new Exp();
        double left = 0;
        double right = 1;
        double step = 0.1;
        double exact = Math.exp(1) - 1;
        double integral;

        do {
            integral = Functions.integrate(f, left, right, step);
            step /= 2;
        } while (Math.abs(integral - exact) > 1e-7);

        System.out.printf("Интеграл e^x = %.10f%n", integral);
        System.out.printf("Шаг = %.10f%n", step * 2);


        System.out.println("\nПроверка итераторов");

        double[] values = {1, 4, 9};
        TabulatedFunction arrayFunc =
                new ArrayTabulatedFunction(0, 2, values);

        FunctionPoint[] points = {
                new FunctionPoint(0, 1),
                new FunctionPoint(1, 4),
                new FunctionPoint(2, 9)
        };
        TabulatedFunction listFunc =
                new LinkedListTabulatedFunction(points);

        System.out.println("ArrayTabulatedFunction:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println(p);
        }

        System.out.println("LinkedListTabulatedFunction:");
        for (FunctionPoint p : listFunc) {
            System.out.println(p);
        }

        System.out.println("\nПроверка фабрик");

        Function cos = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.out.println("\nПроверка рефлексии");

        tf = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(tf.getClass());
        System.out.println(tf);

        tf = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[]{0, 10});
        System.out.println(tf.getClass());
        System.out.println(tf);

        tf = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[]{
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                });
        System.out.println(tf.getClass());
        System.out.println(tf);

        tf = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class,
                new Sin(), 0, Math.PI, 11);
        System.out.println(tf.getClass());
        System.out.println(tf);
    }
}
