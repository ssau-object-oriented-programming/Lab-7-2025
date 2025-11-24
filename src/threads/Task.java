package threads;

import functions.Function;

public class Task {
    private Function function;
    private double left;
    private double right;
    private double step;
    private int count;

    private boolean available = false;

    public Task() {}

    public synchronized void setCount(int count) {
        this.count = count;
    }

    public synchronized int getCount() {
        return count;
    }

    public synchronized void putBlocking(Function function, double left, double right, double step) throws InterruptedException {
        while (available) {
            wait();
        }
        this.function = function;
        this.left = left;
        this.right = right;
        this.step = step;
        available = true;
        notifyAll();
    }

    public synchronized TaskSnapshot takeBlocking() throws InterruptedException {
        while (!available) {
            wait();
        }
        TaskSnapshot snap = new TaskSnapshot(function, left, right, step);
        available = false;
        function = null;
        notifyAll();
        return snap;
    }

    public void putNoSync(Function function, double left, double right, double step) {
        this.function = function;
        this.left = left;
        this.right = right;
        this.step = step;
    }

    public TaskSnapshot takeNoSync() {
        return new TaskSnapshot(function, left, right, step);
    }

    public static class TaskSnapshot {
        public final Function function;
        public final double left;
        public final double right;
        public final double step;

        public TaskSnapshot(Function function, double left, double right, double step) {
            this.function = function;
            this.left = left;
            this.right = right;
            this.step = step;
        }
    }
}
