import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Задание 1. ПРОСТАЯ ПРОВЕРКА ИТЕРАТОРОВ ===\n");
        
        // Тестовые точки
        FunctionPoint[] testPoints = {
            new FunctionPoint(1.0, 2.0),
            new FunctionPoint(2.0, 4.0),
            new FunctionPoint(3.0, 6.0),
            new FunctionPoint(4.0, 8.0),
            new FunctionPoint(5.0, 10.0)
        };
        
        // 1. Проверка ExternalizableArrayTabulatedFunction
        System.out.println("1. ExternalizableArrayTabulatedFunction:");
        TabulatedFunction f1 = new ExternalizableArrayTabulatedFunction(testPoints);
        for (FunctionPoint p : f1) {
            System.out.println("   " + p);
        }

        // 2. Проверка ExternalizableLinkedListTabulatedFunction
        System.out.println("\n2. ExternalizableLinkedListTabulatedFunction:");
        TabulatedFunction f2 = new ExternalizableLinkedListTabulatedFunction(testPoints);
        for (FunctionPoint p : f2) {
            System.out.println("   " + p);
        }

        // 3. Проверка ArrayTabulatedFunction (обычная версия)
        System.out.println("\n3. ArrayTabulatedFunction (обычная):");
        TabulatedFunction f3 = new ArrayTabulatedFunction(testPoints);
        for (FunctionPoint p : f3) {
            System.out.println("   " + p);
        }

        // 4. Проверка LinkedListTabulatedFunction (обычная версия)
        System.out.println("\n4. LinkedListTabulatedFunction (обычная):");
        TabulatedFunction f4 = new LinkedListTabulatedFunction(testPoints);
        for (FunctionPoint p : f4) {
            System.out.println("   " + p);
        }

        System.out.println("\n=== ПРОВЕРКА ЗАВЕРШЕНА ===\n");
    
        // задание 2
        System.out.println("Задание 2. Проверка фабрик:");
        
        Function cosF = new Cos();
        TabulatedFunction tf;
        
        // 1. По умолчанию - ArrayTabulatedFunction
        tf = TabulatedFunctions.tabulate(cosF, 0, Math.PI, 11);
        System.out.println("1. " + tf.getClass().getSimpleName());
        
        // 2. Меняем на LinkedList
        TabulatedFunctions.setTabulatedFunctionFactory(
            new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cosF, 0, Math.PI, 11);
        System.out.println("2. " + tf.getClass().getSimpleName());
        
        // 3. Возвращаем Array
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cosF, 0, Math.PI, 11);
        System.out.println("3. " + tf.getClass().getSimpleName());
        
        // 4. Пробуем ExternalizableArray
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ExternalizableArrayTabulatedFunction.ExternalizableArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cosF, 0, Math.PI, 11);
        System.out.println("4. " + tf.getClass().getSimpleName());
        
        // 5. Пробуем ExternalizableLinkedList
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ExternalizableLinkedListTabulatedFunction.ExternalizableLinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cosF, 0, Math.PI, 11);
        System.out.println("5. " + tf.getClass().getSimpleName());


        //задание 3 
        System.out.println("\n Задание 3 Тестирование рефлексии:\n ");
        
        TabulatedFunction f;
        
        // 1. ArrayTabulatedFunction из интервала
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("1. " + f.getClass().getSimpleName());
        System.out.println("   " + f);
        
        // 2. ArrayTabulatedFunction из значений
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println("\n2. " + f.getClass().getSimpleName());
        System.out.println("   " + f);
        
        // 3. LinkedListTabulatedFunction из точек
        f = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class, 
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(10, 10)
            }
        );
        System.out.println("\n3. " + f.getClass().getSimpleName());
        System.out.println("   " + f);
        
        // 4. Табулирование Sin через рефлексию
        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("\n4. " + f.getClass().getSimpleName());
        System.out.println("   Первые 3 точки:");
        int count = 0;
        for (FunctionPoint point : f) {
            System.out.printf("   (%.3f, %.3f)\n", point.get_x(), point.get_y());
            if (++count >= 3) break;
        }
    }
    
}