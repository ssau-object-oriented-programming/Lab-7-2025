import functions.*;

import functions.basic.*;
import functions.threads.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.Semaphore;


import static java.nio.file.Files.newBufferedWriter;

public class Main {

    public static void main(String[] args) {

        System.out.println("Задание 1");
        System.out.println("   --------------------------------------------------------");
        TabulatedFunction f = new ArrayTabulatedFunction(0, 10, new double[] {0, 1, 4, 9, 16, 25});

        // Использование улучшенного цикла for
        System.out.println("Points in tabulated function:");
        for (FunctionPoint p : f) {
            System.out.println(p);
        }

        // Тестирование с LinkedListTabulatedFunction
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(1, 1),
                        new FunctionPoint(2, 4),
                        new FunctionPoint(3, 9)
                }
        );

        System.out.println("\nPoints in linked list tabulated function:");
        for (FunctionPoint p : linkedListFunc) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println("Задание 2");
        System.out.println("   --------------------------------------------------------");
        Function f1 = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.out.println();
        System.out.println("Задание 3");
        System.out.println("   --------------------------------------------------------");
        TabulatedFunction f2;

        f2 = TabulatedFunctions.createTabulatedFunction( 0, 10, 3, ArrayTabulatedFunction.class);
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.createTabulatedFunction( 0, 10, new double[] {0, 10},ArrayTabulatedFunction.class);
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.tabulate(new Sin(), 0, Math.PI, 11, LinkedListTabulatedFunction.class);
        System.out.println(f2.getClass());
        System.out.println(f2);
    }

}


