import functions.*;
import threads.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try {
            
            demoLab6();
        } catch (Exception e) {
            System.out.println("Ошибка демонстрации lab5: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demoLab6() {
        System.out.println("\n=== Демонстрация лабораторной работы №6 ===");
        
        Function exp = new functions.basic.Exp();
        double exact = Math.exp(1.0) - 1.0;
        System.out.printf("Exact integral of exp on [0,1]: %.12f\n", exact);

        
        double step = 0.5;
        double value;
        for (int iter = 0; iter < 60; iter++) {
            value = Functions.integrate(exp, 0.0, 1.0, step);
            double err = Math.abs(value - exact);
            if (err < 5e-8) {
                System.out.printf("Found step %.12g gives value %.12f (err=%.12g)\n", step, value, err);
                break;
            }
            step /= 2.0;
        }

        
        nonThread();

        
        simpleThreads();

        
        complicatedThreads();
    }

    private static void nonThread() {
        System.out.println("\n--- nonThread() ---");
        Task task = new Task();
        task.setCount(100);
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < task.getCount(); i++) {
            double base = 1.0 + rnd.nextDouble() * 9.0;
            Function f = new functions.basic.Log(base);
            double left = rnd.nextDouble() * 100.0;
            double right = 100.0 + rnd.nextDouble() * 100.0;
            double step = rnd.nextDouble();
            task.putNoSync(f, left, right, step);
            System.out.printf("Source %.6f %.6f %.6f\n", left, right, step);
            double res;
            try {
                res = Functions.integrate(f, left, right, step <= 0 ? 1e-3 : step);
            } catch (IllegalArgumentException ex) {
                res = Double.NaN;
            }
            System.out.printf("Result %.6f %.6f %.6f %.6f\n", left, right, step, res);
        }
    }

    private static void simpleThreads() {
        System.out.println("\n--- simpleThreads() ---");
        Task task = new Task();
        task.setCount(100);
        Thread gen = new Thread(new SimpleGenerator(task));
        Thread intg = new Thread(new SimpleIntegrator(task));
        gen.start();
        intg.start();
        try {
            gen.join();
            intg.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void complicatedThreads() {
        System.out.println("\n--- complicatedThreads() ---");
        Task task = new Task();
        task.setCount(100);
        SimpleSemaphore sem = new SimpleSemaphore();
        Generator g = new Generator(task, sem);
        Integrator it = new Integrator(task, sem);
        g.start();
        it.start();
        
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        g.interrupt();
        it.interrupt();
        try {
            g.join();
            it.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}