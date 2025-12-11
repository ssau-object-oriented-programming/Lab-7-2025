import functions.*;
import functions.basic.*;



public class Main {
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("ЗАДАНИЕ 1: ПРОВЕРКА ИТЕРАТОРА");
        System.out.println("========================================");
        {
            System.out.println("Создаем функцию экспоненты (Exp)");
            Function exp = new Exp();

            System.out.println("Табулируем функцию на отрезке [0, 5] (6 точек)");
            TabulatedFunction f = TabulatedFunctions.tabulate(exp, 0, 5, 6);
            System.out.println("Функция создана: " + f.getClass().getSimpleName());

            System.out.println("Запускаем цикл for-each для перебора точек:");
            for (FunctionPoint p : f) {
                System.out.println("Точка: " + p);
            }
            System.out.println("Итерация завершена успешно.");
        }

        System.out.println("\n========================================");
        System.out.println("ЗАДАНИЕ 2: ПРОВЕРКА ФАБРИК");
        System.out.println("========================================");
        {
            Function f = new Cos();
            TabulatedFunction tf;

            System.out.println("Табулируем Cos с фабрикой о по умолчанию");
            tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
            System.out.println("Результат: создан объект класса " + tf.getClass().getSimpleName());
            // Ожидаем ArrayTabulatedFunction

            System.out.println("\nМеняем фабрику на LinkedList");
            TabulatedFunctions.setTabulatedFunctionFactory(new
                    LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
            System.out.println("Фабрика установлена.");

            System.out.println("Табулируем Cos снова");
            tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
            System.out.println("Результат: создан объект класса " + tf.getClass().getSimpleName());
            // Ожидаем LinkedListTabulatedFunction

            System.out.println("\nМеняем фабрику обратно на Array");
            TabulatedFunctions.setTabulatedFunctionFactory(new
                    ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
            System.out.println("Фабрика установлена.");

            System.out.println("Табулируем Cos в третий раз");
            tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
            System.out.println("Результат: создан объект класса " + tf.getClass().getSimpleName());
            // Ожидаем ArrayTabulatedFunction
        }

        System.out.println("\n========================================");
        System.out.println("ЗАДАНИЕ 3: ПРОВЕРКА РЕФЛЕКСИИ");
        System.out.println("========================================");
        {
            TabulatedFunction f;

            System.out.println("Создаем ArrayTabulatedFunction через рефлексию (по границам и количеству)");
            f = TabulatedFunctions.createTabulatedFunction(
                    ArrayTabulatedFunction.class, 0, 10, 3);
            System.out.println("Класс объекта: " + f.getClass().getSimpleName());
            System.out.println("Содержимое: " + f);

            System.out.println("\nСоздаем ArrayTabulatedFunction через рефлексию (по массивам значений)");
            f = TabulatedFunctions.createTabulatedFunction(
                    ArrayTabulatedFunction.class, 0, 10, new double[]{0, 10});
            System.out.println("Класс объекта: " + f.getClass().getSimpleName());
            System.out.println(" Содержимое: " + f);

            System.out.println("\nСоздаем LinkedListTabulatedFunction через рефлексию (по массиву точек)");
            f = TabulatedFunctions.createTabulatedFunction(
                    LinkedListTabulatedFunction.class,
                    new FunctionPoint[]{
                            new FunctionPoint(0, 0),
                            new FunctionPoint(10, 10)
                    }
            );
            System.out.println("Класс объекта: " + f.getClass().getSimpleName());
            System.out.println("Содержимое: " + f);

            System.out.println("\nТабулируем функцию Sin через рефлексию (хотим получить LinkedList)");
            f = TabulatedFunctions.tabulate(
                    LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
            System.out.println("Класс объекта: " + f.getClass().getSimpleName());
            System.out.println("Содержимое: " + f);
        }
    }
}