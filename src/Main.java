import functions.*;
import functions.basic.*;

public class Main {
    public static void main(String[] args) {
        
        // Задание 1 - Итератор
        System.out.println("Тест Итератора");
        Function fCos = new Cos();
        // Создаем функцию через tabulate (по умолчанию ArrayTabulatedFunction)
        TabulatedFunction tfIterator = TabulatedFunctions.tabulate(fCos, 0, Math.PI, 5);
        // Проверяем цикл for-each
        for (FunctionPoint p : tfIterator) {
            System.out.println(p);
        }

        // Задание 2 - Фабрики
        System.out.println("\nТест Фабрик");
        Function f = new Cos();
        TabulatedFunction tf;
        
        // 1. Используем фабрику по умолчанию (Array)
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика по умолчанию создала: " + tf.getClass().getSimpleName());
        
        // 2. Устанавливаем фабрику LinkedList
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика LinkedList создала: " + tf.getClass().getSimpleName());
        
        // 3. Возвращаем фабрику Array
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика Array создала: " + tf.getClass().getSimpleName());


        // Задание 3 - Рефлексия
        System.out.println("\nТест Рефлексии");
        
        // 1. Создание через класс Array (конструктор left, right, count)
        TabulatedFunction fReflectArray;
        fReflectArray = TabulatedFunctions.createTabulatedFunction(
          ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Через рефлексию создан объект Array: " + fReflectArray.getClass().getSimpleName());
        System.out.println(fReflectArray);

        // 2. Создание через класс Array (конструктор left, right, values)
        fReflectArray = TabulatedFunctions.createTabulatedFunction(
          ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println("Через рефлексию создан объект Array (по значениям): " + fReflectArray.getClass().getSimpleName());
        System.out.println(fReflectArray);

        // 3. Создание через класс LinkedList (конструктор points)
        TabulatedFunction fReflectList;
        fReflectList = TabulatedFunctions.createTabulatedFunction(
          LinkedListTabulatedFunction.class, 
          new FunctionPoint[] {
            new FunctionPoint(0, 0),
            new FunctionPoint(10, 10)
          }
        );
        System.out.println("Через рефлексию создан объект LinkedList (по точкам): " + fReflectList.getClass().getSimpleName());
        System.out.println(fReflectList);

        // 4. Табулирование с указанием класса (LinkedList)
        TabulatedFunction fTabulateReflect = TabulatedFunctions.tabulate(
          LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("Через рефлексию (tabulate) создан объект: " + fTabulateReflect.getClass().getSimpleName());
        System.out.println(fTabulateReflect);
    }
}
