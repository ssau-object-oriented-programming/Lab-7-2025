package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private boolean running = true;
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        while (running) {
            synchronized (task) {
                if (task.isExecuted()) {
                    task.setFunction(new Log(Math.random() * 9 + 1));
                    task.setLeftX(Math.random() * 100);
                    task.setRightX(100 + Math.random() * 100);
                    task.setStepIntegration(Math.random());
                    task.setExecuted(false);
                }
            }
            running = task.getIterations() != task.getCurrentIteration();
        }
    }
}
