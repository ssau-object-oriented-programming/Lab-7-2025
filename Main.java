import functions.*;
import functions.Functions;
import functions.basic.Exp;
import functions.basic.*;
import threads.*;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) { // главный метод программы
        System.out.println("Task 1: ");  // вывод заголовка задания 1

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 15, 14);  // создание табулированной функции
        for (FunctionPoint p:arrayFunc){  // цикл for-each по точкам функции
            System.out.println(p);  // вывод текущей точки
        }

        System.out.println("\nTask 2: ");  // вывод заголовка задания 2
        Function f = new Cos();  // создание функции косинуса
        TabulatedFunction tf;  // объявление переменной для табулированной функции

        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);  // табулирование функции с фабрикой по умолчанию
        System.out.println(tf.getClass());  // вывод типа созданной функции

        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());  // установка фабрики связного списка
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);  // табулирование с новой фабрикой
        System.out.println(tf.getClass());  // вывод типа созданной функции

        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());  // установка фабрики массива
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);  // табулирование с фабрикой массива
        System.out.println(tf.getClass());  // вывод типа созданной функции

        System.out.println("\nTask 3: ");  // вывод заголовка задания 3
        TabulatedFunction f2;  // объявление переменной для табулированной функции

        f2 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);  // создание функции через рефлексию с границами и количеством точек
        System.out.println(f2.getClass());  // вывод типа созданной функции
        System.out.println(f2);  // вывод содержимого функции

        f2 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});  // создание функции через рефлексию с границами и значениями
        System.out.println(f2.getClass());  // вывод типа созданной функции
        System.out.println(f2);  // вывод содержимого функции

        f2 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,  // создание связной функции через рефлексию
                new FunctionPoint[] {  // массив точек
                        new FunctionPoint(0, 0),  // точка начала
                        new FunctionPoint(10, 10)  // точка конца
                }
        );
        System.out.println(f2.getClass());  // вывод типа созданной функции
        System.out.println(f2);  // вывод содержимого функции

        f2 = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);  // табулирование синуса через рефлексию
        System.out.println(f2.getClass());  // вывод типа созданной функции
        System.out.println(f2);  // вывод содержимого функции

    }

    public static void nonThread() { // метод последовательного выполнения
        Task task = new Task(null, 0, 0, 0, 100); // создаем задание на 100 итераций

        for (int i = 0; i < task.getCount(); i++) { // цикл по количеству заданий
            final double base = 1 + Math.random() * 9; // генерируем основание логарифма
            Log logFunction = new Log(base); // создаем логарифмическую функцию

            double leftBorder = Math.random() * 100; // генерируем левую границу
            double rightBorder = 100 + Math.random() * 100; // генерируем правую границу
            double step = Math.random(); // генерируем шаг

            task.setFunc(logFunction); // устанавливаем функцию в задание
            task.setLeftBorder(leftBorder); // устанавливаем левую границу
            task.setRightBorder(rightBorder); // устанавливаем правую границу
            task.setStep(step); // устанавливаем шаг

            System.out.printf("Source %.4f %.4f %.4f%n",
                    task.getLeftBorder(), task.getRightBorder(), task.getStep()); // выводим исходные данные

            try { // пробуем вычислить интеграл
                double result = Functions.integral(task.getFunc(), // вычисляем интеграл
                        task.getLeftBorder(),
                        task.getRightBorder(),
                        task.getStep());

                System.out.printf("Result %.4f %.4f %.4f %.4f%n", // выводим результат
                        task.getLeftBorder(), task.getRightBorder(),
                        task.getStep(), result);
            } catch (IllegalArgumentException e) { // если ошибка в аргументах
                System.out.printf("Error: %s%n", e.getMessage()); // выводим сообщение об ошибке
            }
        }
    }

    public static void simpleThreads() { // метод с простыми потоками
        Task task = new Task(null, 0, 0, 0, 100); // создаем задание на 100 итераций

        Thread generatorThread = new Thread(new SimpleGenerator(task)); // создаем поток генератора
        Thread integratorThread = new Thread(new SimpleIntegrator(task)); // создаем поток интегратора

        generatorThread.setPriority(Thread.MAX_PRIORITY); // устанавливаем высокий приоритет генератору
        integratorThread.setPriority(Thread.MIN_PRIORITY); // устанавливаем низкий приоритет интегратору

        generatorThread.start(); // запускаем поток генератора
        integratorThread.start(); // запускаем поток интегратора

        try { // ждем завершения потоков
            generatorThread.join(); // ждем завершения генератора
            integratorThread.join(); // ждем завершения интегратора
        } catch (InterruptedException e) { // если главный поток прервали
            System.out.println("Main thread was interrupted"); // сообщаем о прерывании
        }
    }

    public static void complicatedThreads() { // метод со сложными потоками
        Task task = new Task(null, 0, 0, 0, 100); // создаем задание на 100 итераций
        Semaphore semaphore = new Semaphore(1); // создаем семафор с одним разрешением

        Generator generator = new Generator(task, semaphore); // создаем генератор с семафором
        Integrator integrator = new Integrator(task, semaphore); // создаем интегратор с семафором

        generator.setPriority(Thread.MAX_PRIORITY); // устанавливаем высокий приоритет генератору
        integrator.setPriority(Thread.MIN_PRIORITY); // устанавливаем низкий приоритет интегратору

        System.out.println("Starting threads with MAX/MIN priorities..."); // сообщаем о запуске
        generator.start(); // запускаем генератор
        integrator.start(); // запускаем интегратор

        try { // ждем 50 миллисекунд
            Thread.sleep(50); // пауза 50 мс
        } catch (InterruptedException e) { // если прервали во время сна
            System.out.println("Main thread sleep interrupted"); // сообщаем
        }

        System.out.println("Interrupting threads after 50ms..."); // сообщаем о прерывании
        generator.interrupt(); // прерываем генератор
        integrator.interrupt(); // прерываем интегратор

        try { // ждем завершения потоков
            generator.join(1000); // ждем генератор не более 1 секунды
            integrator.join(1000); // ждем интегратор не более 1 секунды

            if (generator.isAlive()) { // если генератор еще жив
                System.out.println("Generator did not finish in time"); // сообщаем
            }
            if (integrator.isAlive()) { // если интегратор еще жив
                System.out.println("Integrator did not finish in time"); // сообщаем
            }
        } catch (InterruptedException e) { // если прервали во время ожидания
            System.out.println("Main thread interrupted while joining"); // сообщаем
        }

        System.out.println("complicatedThreads completed"); // сообщаем о завершении
    }
}