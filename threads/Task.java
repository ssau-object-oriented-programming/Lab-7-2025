package threads;

import functions.Function;

public class Task {
    private Function function;
    private double leftBorder;
    private double rightBorder;
    private double step;
    private int tasksCount;
    private boolean dataReady = false;

    public Task(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public Task() {
        this.tasksCount = 0;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    public boolean isDataReady() {
        return dataReady;
    }

    public void setDataReady(boolean dataReady) {
        this.dataReady = dataReady;
    }
}