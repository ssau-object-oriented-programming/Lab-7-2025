import functions.*;
import functions.basic.*;
import functions.meta.*;
import threads.*;

import java.io.*;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] s) throws InappropriateFunctionPointException {
        System.out.println("Задание 1\nfun1:");
        TabulatedFunction fun1 = new ArrayTabulatedFunction(1, 100, 10);
        for (int i = 0; i < fun1.getPointsCount(); i++) {
            fun1.setPointY(i, 3 * fun1.getPointX(i));
        }
        double[] val = {1, 2, 3, 5, 9, 6, 32};
        TabulatedFunction fun2 = new LinkedListTabulatedFunction(1, 7, val);
        for (FunctionPoint p : fun1) {
            System.out.println(p);
        }
        System.out.println("\nfun2:");
        for (FunctionPoint p : fun2) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println("Задание 2");
        Function f1 = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.out.println("Задание 3");

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[]{0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[]{new FunctionPoint(0, 0), new FunctionPoint(10, 10)});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);
    }
}