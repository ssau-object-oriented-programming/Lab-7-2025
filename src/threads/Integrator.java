package threads;

import functions.Functions;
import functions.Function;

public class Integrator extends Thread {
    private final Task task;
    private final SimpleSemaphore sem;

    public Integrator(Task task, SimpleSemaphore sem) {
        this.task = task;
        this.sem = sem;
    }

    @Override
    public void run() {
        int count = task.getCount();
        for (int i = 0; i < count && !isInterrupted(); i++) {
            try {
                sem.acquireReader();
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
            Task.TaskSnapshot snap = task.takeNoSync();
            double left = snap.left;
            double right = snap.right;
            double step = snap.step;
            double res;
            try {
                res = Functions.integrate(snap.function, left, right, step <= 0 ? 1e-3 : step);
            } catch (IllegalArgumentException ex) {
                res = Double.NaN;
            }
            System.out.printf("Result %.6f %.6f %.6f %.6f\n", left, right, step, res);
            sem.releaseReader();
        }
    }
}
