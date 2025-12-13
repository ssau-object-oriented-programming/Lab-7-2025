import functions.*;
import threads.*;
import functions.basic.*;
import java.io.*;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class main {
    public static void main(String[] args) {


        System.out.println("===== Задание 1 =====");
        // Тестирование ArrayTabulatedFunction
        System.out.println("\n=== Тестирование ArrayTabulatedFunction ===");
        FunctionPoint[] points = {
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 1),
            new FunctionPoint(2, 4),
            new FunctionPoint(3, 9),
            new FunctionPoint(4, 16)
        };
        
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);
        System.out.println("Использование for-each цикла:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println(p);
        }
        
        System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===");
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(points);
        System.out.println("Использование for-each цикла:");
        for (FunctionPoint p : linkedListFunc) {
            System.out.println(p);
        }


        System.out.println("===== Задание 2 =====");
        // Тестирование фабрик
        System.out.println("=== Тестирование фабричного метода ===\n");
        
        Function f = new Cos();
        TabulatedFunction tf;
        
        // Изначально используем фабрику по умолчанию (ArrayTabulatedFunction)
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика по умолчанию: " + tf.getClass());
        
        // Меняем на фабрику LinkedListTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки LinkedListTabulatedFunctionFactory: " + tf.getClass());
        
        // Меняем обратно на фабрику ArrayTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки ArrayTabulatedFunctionFactory: " + tf.getClass());
   	
        System.out.println("=== Задание 3 ===\n");
        
        TabulatedFunction ff;
        
        // Создание ArrayTabulatedFunction с помощью рефлексии
        System.out.println("Создание ArrayTabulatedFunction через границы и количество точек:");
        ff = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Класс: " + ff.getClass());
        System.out.println("Функция: " + ff);
        
        // Создание ArrayTabulatedFunction с помощью рефлексии (массив значений)
        System.out.println("\nСоздание ArrayTabulatedFunction через границы и массив значений:");
        ff = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println("Класс: " + ff.getClass());
        System.out.println("Функция: " + ff);
        
        //Создание LinkedListTabulatedFunction с помощью рефлексии (массив точек)
        System.out.println("\nСоздание LinkedListTabulatedFunction через массив точек:");
        ff = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class, 
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(10, 10)
            }
        );
        System.out.println("Класс: " + ff.getClass());
        System.out.println("Функция: " + ff);
        
        //Табулирование функции Sin с помощью рефлексии (создание LinkedListTabulatedFunction)
        System.out.println("\nТабулирование функции Sin с созданием LinkedListTabulatedFunction:");
        ff = TabulatedFunctions.tabulate(
            LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("Класс: " + ff.getClass());
        System.out.println("Функция: " + ff);
        
        
    }
}