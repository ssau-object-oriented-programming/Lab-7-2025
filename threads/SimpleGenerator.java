package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        Random random = new Random();
        for (int i = 0; i < task.taskCount; i++) {


            //  "Гонка данных" (Смешанные данные)
            /*
            task.function = new Log(2 + random.nextDouble() * 9);
            task.left = 0.1 + random.nextDouble() * 100;
             try { Thread.sleep(2); } catch (InterruptedException e) {}//Запись в Result из 2 разных частей
            task.right = 100 + random.nextDouble() * 100;
            task.step = Math.max(0.0001, random.nextDouble());

            System.out.printf("Source: left=%.2f right=%.2f step=%.4f%n",
                    task.left, task.right, task.step);
            */

            //  Устранение проблем с помощью synchronized(Финальная версия)

            synchronized (task) {
                task.function = new Log(2 + random.nextDouble() * 9);
                task.left = 0.1 + random.nextDouble() * 100;
                task.right = 100 + random.nextDouble() * 100;
                task.step = Math.max(0.0001, random.nextDouble());

                System.out.printf("Source: left=%.2f right=%.2f step=%.4f%n",
                        task.left, task.right, task.step);
                try { Thread.sleep(1); } catch (InterruptedException e) {}
           }
        }
    }
}