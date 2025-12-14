
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

        System.out.println("\nТест перегруженных методов с рефлексией");
        
        // Тест 1: tabulate с явным указанием класса
        System.out.println("1. Тест tabulate с рефлексией:");
        TabulatedFunction tf1 = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 5, ArrayTabulatedFunction.class);
        System.out.println("Создана функция типа: " + tf1.getClass().getSimpleName());
        
        TabulatedFunction tf2 = TabulatedFunctions.tabulate(new Cos(), 0, Math.PI, 5, LinkedListTabulatedFunction.class);
        System.out.println("Создана функция типа: " + tf2.getClass().getSimpleName());
        
        // Тест 2: Чтение из байтового потока с указанием класса
        System.out.println("\n2. Тест inputTabulatedFunction с рефлексией:");
        try {
            // Создаем тестовую функцию и сериализуем
            TabulatedFunction original = TabulatedFunctions.createTabulatedFunction( ArrayTabulatedFunction.class, 1, 5, new double[]{1, 4, 9, 16, 25});
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(original, baos);
            byte[] bytes = baos.toByteArray();
            
            // Читаем как LinkedList функцию
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            TabulatedFunction readAsLinkedList = TabulatedFunctions.inputTabulatedFunction(bais, LinkedListTabulatedFunction.class);
            System.out.println("Функция прочитана как: " + readAsLinkedList.getClass().getSimpleName());
            
        } catch (IOException ex)
         { 
            ex.printStackTrace();
        }
        
        // Тест 3: Чтение из текстового потока с указанием класса
        System.out.println("\n3. Тест readTabulatedFunction с рефлексией:");
        try {
            String textData = "3\n0.0\n0.0\n2.0\n4.0\n4.0\n16.0\n";
            StringReader reader = new StringReader(textData);
            
            TabulatedFunction textFunc = TabulatedFunctions.readTabulatedFunction(reader, ArrayTabulatedFunction.class);
            System.out.println("Из текста создана: " + textFunc.getClass().getSimpleName());
            
        } catch (IOException ex) { // Изменил имя переменной с e на ex
            ex.printStackTrace();
        }
    }
}
    
