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
            synchronized (task) {
                task.function = new Log(1 + (random.nextDouble() * 9));
                task.leftX = random.nextDouble() * 100;
                task.rightX = 100 + random.nextDouble() * 100;
                task.step = random.nextDouble();
                
                System.out.println("Source: " + task.leftX + " " + task.rightX + " " + task.step);
            }
            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
