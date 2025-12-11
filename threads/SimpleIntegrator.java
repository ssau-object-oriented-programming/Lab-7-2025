package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.taskCount; i++) {

            // Проблема NullPointerException. Если раскомментировать этот блок, программа упадет, так как task.function может быть null.

            /*
            double res = Functions.integrate(task.function, task.left, task.right, task.step);
            System.out.printf("Result: left=%.2f right=%.2f step=%.4f res=%.6f%n",
                    task.left, task.right, task.step, res);
            */



            // Простое решение (Защита от NullPointerException + Busy Wait)(Сделайте так, чтобы оно не возникало (без синхронизации)+(Демонстрация гонки данных/дубликатов))
            /*
            if (task.function == null) {
                System.out.println("Жду данных...");
                i--; // Шаг назад, чтобы не тратить попытку
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                continue;
            }

            double res = Functions.integrate(task.function, task.left, task.right, task.step);
            System.out.printf("Result: left=%.2f right=%.2f step=%.4f res=%.6f%n",
                    task.left, task.right, task.step, res);

            */
            //Устранение проблем с помощью synchronized(Финальная версия)
            synchronized (task) {
                if (task.function == null) {
                    i--; // Все равно ждем, если попали в блок раньше генератора
                    continue;
                }

                double res = Functions.integrate(task.function, task.left, task.right, task.step);
                System.out.printf("Result: left=%.2f right=%.2f step=%.4f res=%.6f%n",
                        task.left, task.right, task.step, res);
            }

        }
    }
}