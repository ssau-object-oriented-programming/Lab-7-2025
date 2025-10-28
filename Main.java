import functions.*;
import functions.basic.*;
import functions.meta.*;
import threads.*;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
            // Задание 1
        // Создание функции
        TabulatedFunction function = new ArrayTabulatedFunction(0, 10, new double[]{1, 2, 3, 4, 5});

        // Использование в цикле for-each
        for (FunctionPoint point : function) {
            println("Point: " + point);
        }

        // Или с явным итератором
        Iterator<FunctionPoint> iterator = function.iterator();
        while (iterator.hasNext()) {
            FunctionPoint point = iterator.next();
            println("Point: " + point);
            // iterator.remove(); // Выбросит UnsupportedOperationException
        }

        println();

            // Задание 2
        Function fs = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(fs, 0, Math.PI, 11);
        println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(fs, 0, Math.PI, 11);
        println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(fs, 0, Math.PI, 11);
        println(tf.getClass());

        println();
            // Задание 3
        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        println(f.getClass());
        println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        println(f.getClass());
        println(f);

        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, 
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        println(f.getClass());
        println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        println(f.getClass());
        println(f);
    }
//----------------------------------------------------Прочая лабуда-----------------------------------------------------

    /** Короткий вывод
     * @param o то, что будет выводиться
     */
    private static void println(Object o) {
        System.out.println(o);
    }

    /** Пустая Строка
     */
    private static void println() {
        println("");
    }
}