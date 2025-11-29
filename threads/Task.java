package threads;
import functions.Function;

public class Task
{
    private Function func;
    private double leftBorderX;
    private double rightBorderX;
    private double discretizationStep;
    private int taskCount;
    private boolean isReady;

    public Task(int taskCount)
    {
        this.taskCount = taskCount;
    }

    public Function getFunc() {
        return func;
    }

    public void setFunc(Function func) {
        this.func = func;
    }

    public double getLeftBorderX() {
        return leftBorderX;
    }

    public void setLeftBorderX(double leftBorderX) {
        this.leftBorderX = leftBorderX;
    }

    public double getRightBorderX() {
        return rightBorderX;
    }

    public void setRightBorderX(double rightBorderX) {
        this.rightBorderX = rightBorderX;
    }

    public double getDiscretizationStep() {
        return discretizationStep;
    }

    public void setDiscretizationStep(double discretizationStep) {
        this.discretizationStep = discretizationStep;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
