package threads;

import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;
    private boolean running = true;
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        while (running && !isInterrupted()) {
            semaphore.acquireUninterruptibly();
            if (task.isExecuted()) {
                task.setFunction(new Log(Math.random() * 9 + 1));
                task.setLeftX(Math.random() * 100);
                task.setRightX(100 + Math.random() * 100);
                task.setStepIntegration(Math.random());
                task.setExecuted(false);
            }
            running = task.getIterations() != task.getCurrentIteration();
            semaphore.release();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }
}
