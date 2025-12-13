package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                // выносим вычисление случайных значений вне синхронизации
                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                double leftBorder = random.nextDouble() * 100;
                double rightBorder = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                synchronized (task) {
                    task.setFunction(logFunction);
                    task.setLeftBorder(leftBorder);
                    task.setRightBorder(rightBorder);
                    task.setStep(step);

                    System.out.printf("Source %.4f %.4f %.4f\n",
                            leftBorder, rightBorder, step);
                }

                // короткая пауза
                Thread.sleep(1);

            } catch (Exception e) {
                return;
            }
        }
    }
}
