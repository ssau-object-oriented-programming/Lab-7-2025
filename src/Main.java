import functions.Functions;
import functions.basic.Exp;
import functions.basic.Log;
import functions.basic.Sin;
import functions.basic.Cos;
import functions.*;
import functions.meta.*;
import threads.*;

import java.util.Random;
import java.io.*;

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

        // === Новое: Тестирование чтения через рефлексию ===
        System.out.println("\n=== Тестирование чтения через рефлексию ===");
        testReflectionReading();
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

    // Метод для Лабы 7: тестирование итераторов, фабрик и рефлексии
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

        // Тест 6: Обработка ошибок рефлексии
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

    // НОВЫЙ МЕТОД: Тестирование методов чтения через рефлексию
    public static void testReflectionReading() {
        System.out.println("\n=== Тестирование методов чтения через рефлексию ===");

        try {
            // Тест 1: Создаем тестовую функцию для записи и чтения
            System.out.println("\nТест 1: Подготовка тестовой функции");
            TabulatedFunction originalFunc = new ArrayTabulatedFunction(
                    new FunctionPoint[] {
                            new FunctionPoint(0, 0),
                            new FunctionPoint(1, 1),
                            new FunctionPoint(2, 4),
                            new FunctionPoint(3, 9),
                            new FunctionPoint(4, 16)
                    }
            );

            System.out.println("Исходная функция (ArrayTabulatedFunction): " + originalFunc);
            System.out.println("Количество точек: " + originalFunc.getPointsCount());

            // Тест 2: Запись и чтение через OutputStream/InputStream
            System.out.println("\nТест 2: Бинарная сериализация (OutputStream/InputStream)");

            // Записываем функцию в ByteArrayOutputStream
            System.out.println("Запись функции в ByteArrayOutputStream...");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(originalFunc, baos);
            byte[] binaryData = baos.toByteArray();
            System.out.println("Записано " + binaryData.length + " байт");

            // Читаем через рефлексию как ArrayTabulatedFunction
            System.out.println("\nЧтение как ArrayTabulatedFunction...");
            ByteArrayInputStream bais1 = new ByteArrayInputStream(binaryData);
            TabulatedFunction readArrayFunc = TabulatedFunctions.inputTabulatedFunction(
                    ArrayTabulatedFunction.class, bais1);
            System.out.println("Прочитано: " + readArrayFunc);
            System.out.println("Равны исходной? " + originalFunc.equals(readArrayFunc));

            // Читаем через рефлексию как LinkedListTabulatedFunction
            System.out.println("\nЧтение как LinkedListTabulatedFunction...");
            ByteArrayInputStream bais2 = new ByteArrayInputStream(binaryData);
            TabulatedFunction readLinkedFunc = TabulatedFunctions.inputTabulatedFunction(
                    LinkedListTabulatedFunction.class, bais2);
            System.out.println("Прочитано: " + readLinkedFunc);
            System.out.println("Равны исходной? " + originalFunc.equals(readLinkedFunc));

            // Сравниваем результаты между собой
            System.out.println("\nArray и LinkedList равны между собой? " +
                    readArrayFunc.equals(readLinkedFunc));

            // Тест 3: Запись и чтение через Writer/Reader
            System.out.println("\nТест 3: Текстовая сериализация (Writer/Reader)");

            // Записываем функцию в StringWriter
            System.out.println("Запись функции в StringWriter...");
            StringWriter sw = new StringWriter();
            TabulatedFunctions.writeTabulatedFunction(originalFunc, sw);
            String textData = sw.toString();
            System.out.println("Текстовые данные: " + textData.trim());

            // Читаем через рефлексию как ArrayTabulatedFunction
            System.out.println("\nЧтение как ArrayTabulatedFunction...");
            StringReader sr1 = new StringReader(textData);
            TabulatedFunction readArrayFunc2 = TabulatedFunctions.readTabulatedFunction(
                    ArrayTabulatedFunction.class, sr1);
            System.out.println("Прочитано: " + readArrayFunc2);
            System.out.println("Равны исходной? " + originalFunc.equals(readArrayFunc2));

            // Читаем через рефлексию как LinkedListTabulatedFunction
            System.out.println("\nЧтение как LinkedListTabulatedFunction...");
            StringReader sr2 = new StringReader(textData);
            TabulatedFunction readLinkedFunc2 = TabulatedFunctions.readTabulatedFunction(
                    LinkedListTabulatedFunction.class, sr2);
            System.out.println("Прочитано: " + readLinkedFunc2);
            System.out.println("Равны исходной? " + originalFunc.equals(readLinkedFunc2));

            // Тест 4: Обработка ошибок - некорректные данные
            System.out.println("\nТест 4: Обработка ошибок (некорректные данные)");

            try {
                System.out.println("Попытка чтения из пустого потока...");
                ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
                TabulatedFunction badFunc = TabulatedFunctions.inputTabulatedFunction(
                        ArrayTabulatedFunction.class, emptyStream);
                System.out.println("ОШИБКА: Должно было быть исключение!");
            } catch (IOException e) {
                System.out.println("✓ Ожидаемое исключение: " + e.getClass().getSimpleName());
                System.out.println("  Сообщение: " + e.getMessage());
            }

            // Тест 5: Обработка ошибок - неверный класс
            try {
                System.out.println("\nПопытка создания с интерфейсом вместо класса...");
                byte[] testData = {0, 0, 0, 2, 64, 0, 0, 0, 0, 0, 0, 0, 64, 8, 0, 0, 0, 0, 0, 0};
                ByteArrayInputStream bais3 = new ByteArrayInputStream(testData);
                TabulatedFunction badFunc = TabulatedFunctions.inputTabulatedFunction(
                        TabulatedFunction.class, bais3); // Интерфейс, а не класс
                System.out.println("ОШИБКА: Должно было быть исключение!");
            } catch (Exception e) {
                System.out.println("✓ Ожидаемое исключение: " + e.getClass().getSimpleName());
                System.out.println("  Сообщение: " + e.getMessage());
                if (e.getCause() != null) {
                    System.out.println("  Причина: " + e.getCause().getClass().getSimpleName());
                }
            }

            // Тест 6: Сравнение старых и новых методов
            System.out.println("\nТест 6: Сравнение старых и новых методов чтения");

            // Используем старый метод (через фабрику)
            ByteArrayInputStream bais4 = new ByteArrayInputStream(binaryData);
            TabulatedFunction oldWayFunc = TabulatedFunctions.inputTabulatedFunction(bais4);
            System.out.println("Старый метод (фабрика): " + oldWayFunc.getClass().getSimpleName());

            // Используем новый метод с рефлексией
            ByteArrayInputStream bais5 = new ByteArrayInputStream(binaryData);
            TabulatedFunction newWayFunc = TabulatedFunctions.inputTabulatedFunction(
                    LinkedListTabulatedFunction.class, bais5);
            System.out.println("Новый метод (рефлексия): " + newWayFunc.getClass().getSimpleName());

            System.out.println("Типы отличаются? " +
                    (!oldWayFunc.getClass().equals(newWayFunc.getClass())));

            // Тест 7: Чтение сложной функции
            System.out.println("\nТест 7: Чтение функции с большим количеством точек");

            // Создаем синусоиду с 20 точками
            TabulatedFunction sineFunc = TabulatedFunctions.tabulate(
                    new Sin(), 0, 2 * Math.PI, 20);

            // Записываем
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(sineFunc, baos2);

            // Читаем как LinkedListTabulatedFunction
            ByteArrayInputStream bais6 = new ByteArrayInputStream(baos2.toByteArray());
            TabulatedFunction readSineFunc = TabulatedFunctions.inputTabulatedFunction(
                    LinkedListTabulatedFunction.class, bais6);

            System.out.println("Исходная функция точек: " + sineFunc.getPointsCount());
            System.out.println("Прочитанная функция точек: " + readSineFunc.getPointsCount());
            System.out.println("Функции равны? " + sineFunc.equals(readSineFunc));

            // Выводим несколько точек для проверки
            System.out.println("\nПервые 5 точек для сравнения:");
            System.out.println("Исходная:  " +
                    String.format("(%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f)",
                            sineFunc.getPointX(0), sineFunc.getPointY(0),
                            sineFunc.getPointX(1), sineFunc.getPointY(1),
                            sineFunc.getPointX(2), sineFunc.getPointY(2),
                            sineFunc.getPointX(3), sineFunc.getPointY(3),
                            sineFunc.getPointX(4), sineFunc.getPointY(4)));
            System.out.println("Прочитанная: " +
                    String.format("(%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f), (%.3f; %.3f)",
                            readSineFunc.getPointX(0), readSineFunc.getPointY(0),
                            readSineFunc.getPointX(1), readSineFunc.getPointY(1),
                            readSineFunc.getPointX(2), readSineFunc.getPointY(2),
                            readSineFunc.getPointX(3), readSineFunc.getPointY(3),
                            readSineFunc.getPointX(4), readSineFunc.getPointY(4)));

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка при тестировании чтения: " + e);
            e.printStackTrace();
        }

        System.out.println("\n=== Тестирование чтения через рефлексию завершено ===");
    }
}
