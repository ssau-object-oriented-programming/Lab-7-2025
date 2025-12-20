import functions.*;
import functions.basic.*;

public class Main {
    public static void main(String[] args) {

        //1
        System.out.println("\nТест итератора");

        FunctionPoint[] points = {
                new FunctionPoint(3, 9),
                new FunctionPoint(5, 25),
                new FunctionPoint(7.5, 56.25)
        };

        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(points);
        LinkedListTabulatedFunction listFunction = new LinkedListTabulatedFunction(points);

        System.out.println("Итерация по ArrayTabulatedFunction:");
        for (FunctionPoint p : arrayFunction) {
            System.out.println("  " + p);
        }

        System.out.println("\nИтерация по LinkedListTabulatedFunction:");
        for (FunctionPoint p : listFunction) {
            System.out.println("  " + p);
        }

        //2
        System.out.println("\n\nТест фабрик");
        Function f = new Cos();

        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика по умолчанию: " + tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Array фабрика: " + tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("LinkedList фабрика: " + tf.getClass());

        //3
        System.out.println("\n\nТест рефлексии");
        TabulatedFunction f1;

        f1 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                new FunctionPoint[] {new FunctionPoint(0, 10), new FunctionPoint(10,10)});
        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class,
                new Sin(),0, Math.PI, 11);
        System.out.println(f1.getClass());
        System.out.println(f1);
    }

}


