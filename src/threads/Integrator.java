package threads;

import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final SimpleSemaphore semaphore;

    public Integrator(Task task, SimpleSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        while (!isInterrupted() && task.taskCount >= 0) { 
            try {
                semaphore.beginRead();
                
                if (task.function == null) {
                     semaphore.endRead();
                     break; 
                }

                double result = Functions.integrate(task.function, task.leftX, task.rightX, task.step);
                System.out.println("Result: " + task.leftX + " " + task.rightX + " " + task.step + " = " + result);
                
                semaphore.endRead();

            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
