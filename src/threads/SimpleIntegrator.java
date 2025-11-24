package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        int count = task.getCount();
        for (int i = 0; i < count; i++) {
            Task.TaskSnapshot snap;
            try {
                snap = task.takeBlocking();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            Function f = snap.function;
            double left = snap.left;
            double right = snap.right;
            double step = snap.step;
            double result;
            try {
                result = Functions.integrate(f, left, right, step <= 0 ? 1e-3 : step);
            } catch (IllegalArgumentException ex) {
                result = Double.NaN;
            }
            System.out.printf("Result %.6f %.6f %.6f %.6f\n", left, right, step, result);
        }
    }
}
