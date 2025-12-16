import functions.Functions;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Sin;
import functions.basic.Cos;
import functions.*;
import functions.meta.*;
import threads.*;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // Задание 1: Проверка интеграла экспоненты
        System.out.println("=== Задание 1: Интеграл exp(x) от 0 до 1 ===");
        double leftExp = 0.0;
        double rightExp = 1.0;

        double stepExp = 0.1;
        double theoretical = Math.exp(1) - 1;
        double resultExp;
        do {
            resultExp = Functions.integrate(new Exp(), leftExp, rightExp, stepExp);
            stepExp /= 2;
        } while (Math.abs(resultExp - theoretical) > 1e-7);

        System.out.printf("Интеграл exp(x) от %.2f до %.2f с шагом %.10f = %.10f%n",
                leftExp, rightExp, stepExp, resultExp);

        // === Задание 2: Последовательное выполнение 100 логарифмических интегралов ===
        System.out.println("\n Задание 2: 100 случайных логарифмических интегралов ");
        nonThread();

        // === Задание 3: Потоковое выполнение ===
        System.out.println("\n Задание 3: Потоковая генерация и интегрирование ");
        simpleThreads();

        // === Задание 4: Потоковое выполнение с семафором и прерыванием ===
        System.out.println("\n Задание 4: Потоки с семафором и прерыванием ");
        complicatedThreads();

        // === Задание 5: Лабораторная работа №7 ===
        System.out.println("\n=== Лабораторная работа №7 ===");
        testLab7();
    }

    // Метод последовательного выполнения (Задание 2)
    public static void nonThread() {
        Random rand = new Random();
        Task task = new Task();
        task.setTaskCount(100);

        System.out.println("Количество заданий: " + task.getTaskCount());

        for (int i = 0; i < task.getTaskCount(); i++) {
            int taskNumber = i + 1;

            double base = 1 + 9 * rand.nextDouble();
            double left = 100 * rand.nextDouble();
            double right = left + 100 * rand.nextDouble();
            double step = 0.01 + 0.99 * rand.nextDouble();

            task.setFunction(new Log(base));
            task.setLeftBound(left);
            task.setRightBound(right);
            task.setDiscretizationStep(step);

            System.out.printf("Task %d - Source: %.6f %.6f %.6f%n", taskNumber, left, right, step);

            try {
                double result = Functions.integrate(task.getFunction(), left, right, step);
                System.out.printf("Task %d - Result: %.6f %.6f %.6f %.6f%n", taskNumber, left, right, step, result);
            } catch (IllegalArgumentException e) {
                System.out.printf("Task %d - Ошибка интегрирования: %s%n", taskNumber, e.getMessage());
            }
        }
    }

    // Метод потокового выполнения (Задание 3)
    public static void simpleThreads() {
        Task task = new Task();
        task.setTaskCount(100);

        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));

        generatorThread.setPriority(Thread.MAX_PRIORITY);
        integratorThread.setPriority(Thread.MIN_PRIORITY);

        generatorThread.start();
        integratorThread.start();

        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все задачи сгенерированы и интегрированы потоками.");
    }

    // Метод для Задания 4
    public static void complicatedThreads() {
        Task task = new Task();
        task.setTaskCount(100);

        Semaphore semaphore = new Semaphore();

        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        generator.start();
        integrator.start();

        try {
            // Ждём завершения потоков (без прерывания)
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все задачи успешно обработаны с использованием семафора.");
    }

    // НОВЫЙ МЕТОД ДЛЯ ЛАБЫ 7
    public static void testLab7() {
        System.out.println("\n=== Тестирование итераторов ===");

        // Тест 1: Итератор для ArrayTabulatedFunction
        System.out.println("Тест 1: Итерация по ArrayTabulatedFunction");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, 5);
        System.out.println("Точки функции:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println("  " + p);
        }

        // Тест 2: Итератор для LinkedListTabulatedFunction
        System.out.println("\nТест 2: Итерация по LinkedListTabulatedFunction");
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 10, 5);
        System.out.println("Точки функции:");
        for (FunctionPoint p : linkedFunc) {
            System.out.println("  " + p);
        }

        System.out.println("\n=== Тестирование фабрик ===");

        // Тест 3: Фабричный метод (создание через фабрику)
        System.out.println("Тест 3: Использование фабрики");
        Function cosFunc = new Cos();

        // Устанавливаем фабрику для ArrayTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        TabulatedFunction tf1 = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 11);
        System.out.println("Фабрика Array: " + tf1.getClass().getSimpleName());

        // Меняем на фабрику для LinkedListTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        TabulatedFunction tf2 = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 11);
        System.out.println("Фабрика LinkedList: " + tf2.getClass().getSimpleName());

        // Возвращаем обратно
        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());

        System.out.println("\n=== Тестирование рефлексии ===");

        // Тест 4: Создание через рефлексию
        System.out.println("Тест 4: Создание через рефлексию");

        TabulatedFunction f1 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("1. ArrayTabulatedFunction (3 точки): " + f1.getClass().getSimpleName());
        System.out.println("   " + f1);

        TabulatedFunction f2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println("2. ArrayTabulatedFunction (значения): " + f2.getClass().getSimpleName());
        System.out.println("   " + f2);

        TabulatedFunction f3 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(5, 25),
                        new FunctionPoint(10, 100)
                }
        );
        System.out.println("3. LinkedListTabulatedFunction (точки): " + f3.getClass().getSimpleName());
        System.out.println("   " + f3);

        // Тест 5: Tabulate с указанием класса
        System.out.println("\nТест 5: Tabulate с указанием класса через рефлексию");
        TabulatedFunction f4 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("Tabulate с LinkedListTabulatedFunction.class: " + f4.getClass().getSimpleName());
        System.out.println("Количество точек: " + f4.getPointsCount());

        // Тест 6: Обработка ошибок
        System.out.println("\nТест 6: Обработка ошибок рефлексии");
        try {
            TabulatedFunction f5 = TabulatedFunctions.createTabulatedFunction(
                    TabulatedFunction.class, 0, 10, 3); // Неправильный класс (интерфейс)
            System.out.println("ОШИБКА: Должно было быть исключение!");
        } catch (Exception e) {
            System.out.println("Ожидаемое исключение: " + e.getClass().getSimpleName());
            System.out.println("Сообщение: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Причина: " + e.getCause().getClass().getSimpleName());
            }
        }

        System.out.println("\n=== Все тесты лабораторной работы №7 завершены ===");
    }
}