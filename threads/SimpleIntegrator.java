package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            synchronized (task) {
                // вычисляем значение интеграла для параметров из объекта задания
                double result = Functions.integrate(task.getFunction(),
                        task.getLeftBorder(), task.getRightBorder(), task.getStep());

                System.out.printf("Result %.4f %.4f %.4f %.4f\n",
                        task.getLeftBorder(), task.getRightBorder(), task.getStep(), result);
            }

            // пауза чтобы генерирующий поток положил новые данные
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("интегрирующий поток был прерван");
                return;
            }
        }
    }
}


