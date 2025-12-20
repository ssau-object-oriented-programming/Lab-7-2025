import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Задание 1.");
        System.out.println("Тестирование итератора ArrayTabulatedFunction:");
        FunctionPoint[] points1 = {
                new FunctionPoint(0, 0),
                new FunctionPoint(1, 1),
                new FunctionPoint(2, 4)
        };
        TabulatedFunction f1 = new ArrayTabulatedFunction(points1);
        for (FunctionPoint p : f1) {
            System.out.println(p);
        }
        System.out.println("\nТестирование итератора LinkedListTabulatedFunction:");
        TabulatedFunction f2 = new LinkedListTabulatedFunction(points1);
        for (FunctionPoint p : f2) {
            System.out.println(p);
        }

        System.out.println("\n\nЗадание 2.");
        Function f = new functions.basic.Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика по умолчанию: " + tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика LinkedListTabulatedFunction: " + tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика ArrayTabulatedFunction: " + tf.getClass());

        System.out.println("\n\nЗадание 3.");
        TabulatedFunction f3 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Тип: " + f3.getClass());
        System.out.println("Функция: " + f3);
        TabulatedFunction f4 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println("\nТип: " + f4.getClass());
        System.out.println("Функция: " + f4);
        FunctionPoint[] points = {
                new FunctionPoint(0, 0),
                new FunctionPoint(10, 10)
        };
        TabulatedFunction f5 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class, points);
        System.out.println("\nТип: " + f5.getClass());
        System.out.println("Функция: " + f5);
        TabulatedFunction f6 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new functions.basic.Sin(), 0, Math.PI, 11);
        System.out.println("\nТип: " + f6.getClass());
        System.out.println("Функция: " + f6);
    }
}
