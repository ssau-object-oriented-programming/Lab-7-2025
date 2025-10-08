package threads;

import functions.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private final Semaphore semaphore;
    private final Task task;
    private boolean running = true;
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        while (running && !isInterrupted()) {
            semaphore.acquireUninterruptibly();
            if (!task.isExecuted()) {
                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStepIntegration());
                double value = Functions.findIntegral(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStepIntegration());
                System.out.println("№" + task.getCurrentIteration() + ". Result " + task.getLeftX() + " " + task.getRightX() + " " + task.getStepIntegration() + " " + value);

                task.incrementIteration();
            }
            running = task.getCurrentIteration() != task.getIterations();
            semaphore.release();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }
}
