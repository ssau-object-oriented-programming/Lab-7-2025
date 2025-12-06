import functions.*;
import functions.basic.Exp;
import functions.basic.Log;
import threads.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        
        // --- Задание 1: Интеграл ---
        System.out.println("--- Задание 1: Интегрирование экспоненты ---");
        Function exp = new Exp();
        double res = Functions.integrate(exp, 0, 1, 0.1); 
        double theoretical = Math.E - 1; 
        System.out.println("Результат (шаг 0.1): " + res);
        System.out.println("Точное значение:     " + theoretical);
        
        double step = 0.1;
        while (Math.abs(Functions.integrate(exp, 0, 1, step) - theoretical) >= 1e-7) {
            step /= 2;
        }
        System.out.println("Шаг для точности 1e-7: " + step);
        System.out.println("Результат с этим шагом: " + Functions.integrate(exp, 0, 1, step));


        // --- Задание 2: NonThread ---
        System.out.println("\n--- Задание 2: nonThread() ---");
        //nonThread();

        // --- Задание 3: Simple Threads ---
        System.out.println("\n--- Задание 3: simpleThreads() ---");
        simpleThreads();

        // --- Задание 4: Complicated Threads (Semaphore) ---
        System.out.println("\n--- Задание 4: complicatedThreads() ---");
        complicatedThreads();
    }

    public static void nonThread() {
        Task task = new Task(100);
        Random random = new Random();
        for (int i = 0; i < task.taskCount; i++) {
            task.function = new Log(1 + (random.nextDouble() * 9));
            task.leftX = random.nextDouble() * 100;
            task.rightX = 100 + random.nextDouble() * 100;
            task.step = random.nextDouble();
            
            System.out.println("Source: " + task.leftX + " " + task.rightX + " " + task.step);
            
            double r = Functions.integrate(task.function, task.leftX, task.rightX, task.step);
            System.out.println("Result: " + task.leftX + " " + task.rightX + " " + task.step + " " + r);
        }
    }

    public static void simpleThreads() {
        Task task = new Task(100);
        SimpleGenerator generator = new SimpleGenerator(task);
        SimpleIntegrator integrator = new SimpleIntegrator(task);

        Thread t1 = new Thread(generator);
        Thread t2 = new Thread(integrator);

        // Запускаем потоки ОДНОВРЕМЕННО, без задержек
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void complicatedThreads() {
        Task task = new Task(100);
        SimpleSemaphore semaphore = new SimpleSemaphore();
        
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50); 
            generator.interrupt();
            integrator.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
