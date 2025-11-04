package threads;
import functions.basic.*;

public class SimpleGenerator implements Runnable {
    private Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); ++i) {
            synchronized (task) {
                while (task.getFunction() != null) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                task.setFunction(new Log(Math.random() * 10 + 1));
                task.setLeftX(Math.random() * 101);
                task.setRightX(Math.random() * 101 + 100);
                task.setStep(Math.random());
                System.out.printf("Source левая граница: %f правая граница: %f шаг дискретизации: %f\n", task.getLeftX(), task.getRightX(), task.getStep());
                task.notifyAll();
            }
        }
    }
}