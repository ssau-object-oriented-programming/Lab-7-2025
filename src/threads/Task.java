package threads;

import functions.Function;

public class Task {
    public Function function;
    public double leftX;
    public double rightX;
    public double step;
    public double result;
    public int taskCount;

    public Task(int taskCount) {
        this.taskCount = taskCount;
    }
}
