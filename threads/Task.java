package threads;
import functions.*;

public class Task {
    private Function f;
    private double leftX;
    private double rightX;
    private double step;
    private int tasksCount;

    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public void setFunction(Function f) {
        this.f = f;
    }

    public Function getFunction() {
        return f;
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setRightX(double rightX) {
        this.rightX = rightX;
    }

    public double getRightX() {
        return rightX;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setTasksCount(int count) {
        tasksCount = count;
    }

    public int getTasksCount() {
        return tasksCount;
    }
}