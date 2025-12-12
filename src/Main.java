import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        System.out.println("Задание 1: Итератор");

        // Создаем уникальные точки
        FunctionPoint[] myPoints = {
                new FunctionPoint(1.0, 10.0),
                new FunctionPoint(2.0, 20.0),
                new FunctionPoint(3.0, 30.0)
        };

        // Проверяем LinkedList
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(myPoints);
        System.out.println("Перебор списка (LinkedList) через for-each:");
        for (FunctionPoint p : listFunc) {
            System.out.println(p);
        }

        // Проверяем Array и защиту итератора
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(myPoints);
        System.out.println("\nПеребор массива (Array) и тест ошибки удаления:");
        Iterator<FunctionPoint> iterator = arrayFunc.iterator();
        while (iterator.hasNext()) {
            FunctionPoint p = iterator.next();
            System.out.print(p + " ");
        }
        System.out.println();

        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("Успешно: метод remove() выбросил ошибку, как положено.");
        }


        System.out.println("\nЗадание 2: Фабрики" );
        Function sinFunc = new Sin();

        // Устанавливаем фабрику LinkedList
        System.out.println("Используем LinkedListFactory");
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());

        // Табулируем Синус от 0 до PI/2 с 10 точками
        TabulatedFunction tf = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI / 2, 10);
        System.out.println("Создан класс: " + tf.getClass().getSimpleName());

        // ПРОВЕРКА ЗНАЧЕНИЙ
        System.out.println("Проверка значений (Сравнение с Math.sin):");
        boolean isCorrect = true;
        // Проверяем каждую точку
        for (FunctionPoint p : tf) {
            double expected = Math.sin(p.getX());
            // Сравниваем с небольшой погрешностью
            if (Math.abs(p.getY() - expected) > 1e-9) {
                System.out.printf("ОШИБКА: x=%.4f, y=%.4f, ожидалось=%.4f%n", p.getX(), p.getY(), expected);
                isCorrect = false;
            }
        }
        if (isCorrect) {
            System.out.println("Все точки совпадают с эталонным Синусом!");
        }
        // Выведем последнюю точку для наглядности
        System.out.println("Пример последней точки: " + tf.getPointY(tf.getPointsCount() - 1));


        System.out.println("\nЗадание 3: Рефлексия ");
        Function cosFunc = new Cos();

        // 1. Создаем ArrayTabulatedFunction через класс
        System.out.println("Создание через .class (Array)");
        TabulatedFunction fReflect = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 3, 3); // 3 точки: 0, 1.5, 3
        System.out.println("Класс: " + fReflect.getClass().getSimpleName());
        System.out.println("Точки (должны быть (0,0), (1.5,0), (3,0)): " + fReflect);

        // 2. Создаем LinkedListTabulatedFunction через класс с массивом значений
        System.out.println("\nСоздание через .class (LinkedList + значения)");
        double[] values = {11.1, 22.2, 33.3}; // Уникальные значения
        fReflect = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class, 0, 10, values);

        System.out.println("Класс: " + fReflect.getClass().getSimpleName());
        // Проверяем, записались ли значения
        if (Math.abs(fReflect.getPointY(1) - 22.2) < 1e-9) {
            System.out.println("Значение посередине корректно: " + fReflect.getPointY(1));
        } else {
            System.out.println("ОШИБКА: значение не совпало!");
        }

        // 3. Табулируем Косинус через рефлексию
        System.out.println("\nТабулирование Косинуса через рефлексию (LinkedList)");
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, cosFunc, 0, Math.PI, 11);
        System.out.println("Создан класс: " + tabCos.getClass().getSimpleName());

        // ПРОВЕРКА ЗНАЧЕНИЙ
        System.out.println("Проверка значений (Сравнение с Math.cos):");
        isCorrect = true;
        for (FunctionPoint p : tabCos) {
            double expected = Math.cos(p.getX());
            if (Math.abs(p.getY() - expected) > 1e-9) {
                System.out.printf("ОШИБКА: x=%.4f, y=%.4f, ожидалось=%.4f%n", p.getX(), p.getY(), expected);
                isCorrect = false;
            }
        }
        if (isCorrect) {
            System.out.println("Все точки совпадают с эталонным Косинусом!");
        }
        // Выведем значение в точке 0 (должно быть 1.0)
        System.out.println("Значение в точке 0: " + tabCos.getPointY(0));
    }
}