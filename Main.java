
import functions.*;
import functions.basic.*;
import functions.threads.Generator;
import java.util.concurrent.Semaphore;
import functions.threads.Integrator;
import functions.threads.SimpleGenerator;
import functions.threads.SimpleIntegrator;
import functions.threads.Task;
import java.beans.Expression;
import java.io.*;
import java.util.Random;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Main {
    public static void main(String[] args) {
        System.out.println("Тест задания 1");
        // Тест 1: ArrayTabulatedFunction с for-each
        System.out.println("Тест 1: ArrayTabulatedFunction");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16, 25});
        
        System.out.println("Точки функции (for-each):");
        for (FunctionPoint p : arrayFunc) {
            System.out.println(p);
        }
        
        // Тест 2: LinkedListTabulatedFunction с for-each
        System.out.println("\nТест 2: LinkedListTabulatedFunction");
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 4, new double[]{0, 1, 8, 27, 64});
        
        System.out.println("Точки функции (for-each):");
        for (FunctionPoint p : listFunc) {
            System.out.println(p);
        }
        
        // Тест 3: Проверка инкапсуляции
        System.out.println("\nТест 3: Проверка инкапсуляции");
        Iterator<FunctionPoint> iterator = arrayFunc.iterator();
        FunctionPoint firstCopy = iterator.next();
        firstCopy.setX(999);
        firstCopy.setY(999);
        
        System.out.println("Измененная копия первой точки: " + firstCopy);
        System.out.println("Оригинальная первая точка: " + arrayFunc.getPoint(0));
        System.out.println("Инкапсуляция сохранена: " + 
            !firstCopy.equals(arrayFunc.getPoint(0)));
        
        // Тест 4: Проверка исключения remove()
        System.out.println("\nТест 4: Проверка remove()");
        iterator = listFunc.iterator();
        iterator.next(); // Пропускаем первый элемент
        
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException поймано: " + 
                e.getMessage());
        }
        
        // Тест 5: Проверка NoSuchElementException
        System.out.println("\nТест 5: Проверка NoSuchElementException");
        iterator = arrayFunc.iterator();
        
        // Пропускаем все элементы
        while (iterator.hasNext()) {
            iterator.next();
        }
        
        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException поймано: " + 
                e.getMessage());
        }
         System.out.println("\nТест задания 2");

        Function f = new Cos();
        TabulatedFunction tf;
        
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
    
        System.out.println("\nТест задания 3");

        TabulatedFunction f1;
        
        System.out.println("=== Тестирование рефлексивного создания ===");
        
        // 1. ArrayTabulatedFunction через рефлексию (double, double, int)
        f1 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("1. " + f1.getClass().getSimpleName());
        System.out.println("   " + f1);
        
        // 2. ArrayTabulatedFunction через рефлексию (double, double, double[])
        f1 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println("\n2. " + f1.getClass().getSimpleName());
        System.out.println("   " + f1);
        
        // 3. LinkedListTabulatedFunction через рефлексию (FunctionPoint[])
        f1 = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class, 
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(5, 25),
                new FunctionPoint(10, 100)
            }
        );
        System.out.println("\n3. " + f1.getClass().getSimpleName());
        System.out.println("   " + f1);
        
        // 4. tabulate с Sin функцией (использует текущую фабрику)
        f1 = TabulatedFunctions.tabulate(new Sin(), 0, Math.PI, 11);
        System.out.println("\n4. tabulate с Sin (через фабрику): " + f1.getClass().getSimpleName());
        System.out.println("   " + f1);
    }
}
    
    
