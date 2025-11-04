package threads;
import functions.*;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); ++i) {
            synchronized (task) {
                while (task.getFunction() == null) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.printf("Result левая граница: %f правая граница: %f шаг дискретизации: %f результат интегрирования: %f\n", task.getLeftX(), task.getRightX(), task.getStep(), Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStep()));
                task.setFunction(null);
                task.notifyAll();
            }
        }
    }
}