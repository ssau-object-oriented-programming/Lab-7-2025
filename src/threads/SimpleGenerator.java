package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        int taskCount = task.getTaskCount();

        for (int i = 0; i < taskCount; i++) {

            double base = 1 + Math.random() * 9;
            double left = Math.random() * 100;
            double right = 100 + Math.random() * 100;
            double step = Math.random();

            Log logFunction = new Log(base);

            synchronized (task) {
                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setDiscretStep(step);
            }
            System.out.printf("Generator [%d]: Source %.4f %.4f %.4f%n", i+1, left, right, step);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Generator завершен");
    }
}
