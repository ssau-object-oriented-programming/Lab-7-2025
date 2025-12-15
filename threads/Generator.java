package threads;
import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;
    private final Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (isInterrupted()) {
                System.out.println("Generator прерван");
                break;
            }
            // Генерация параметров задания
            double base = 1.0 + random.nextDouble() * 9.0; // [1,10)
            double left = random.nextDouble() * 100.0; // [0,100)
            double right = 100.0 + random.nextDouble() * 100.0; // [100,200)
            double step = random.nextDouble(); // [0,1)
            try {
                // Запись параметров задания под защитой семафора
                semaphore.beginWrite();
                try {
                    task.setFunction(new Log(base));
                    task.setLeft(left);
                    task.setRight(right);
                    task.setStep(step);
                } finally {
                    semaphore.endWrite();
                }
            } catch (InterruptedException e) {
                System.out.println("Generator прерван при ожидании семафора");
                interrupt();
                break;
            }
            // Сообщение "Source левая правая шаг"
            System.out.printf("Source %.6f %.6f %.6f%n", left, right, step);
            // Небольшая пауза для наглядности
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Generator прерван во время sleep");
                interrupt();
                break;
            }
        }
    }
}