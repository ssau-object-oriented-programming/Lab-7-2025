package threads;

import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Random random = new Random();
        int tasksCount = task.getTasksCount();

        for (int i = 0; i < tasksCount; i++) {
            synchronized (task) {
                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                task.setFunction(logFunction);

                double left = random.nextDouble() * 100;
                task.setLeft(left);

                double right = 100 + random.nextDouble() * 100;
                task.setRight(right);

                double step = random.nextDouble();
                task.setStep(step);

                System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);
            }
            try {
                Thread.sleep(2); // небольшая пауза для интегратора
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
