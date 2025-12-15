package threads;

import functions.Function;

public class Task {
    // поля должны быть volatile для многопоточности
    private volatile Function function;
    private volatile double leftBorder;
    private volatile double rightBorder;
    private volatile double discretizationStep;
    private final int tasksCount;

    private volatile boolean dataProcessed = true;

    public Task() {
        this.tasksCount = 100;
    }

    public Task(int tasksCount) {
        if (tasksCount < 1) {
            throw new IllegalArgumentException("Количество заданий должно быть положительным");
        }
        this.tasksCount = tasksCount;
    }

    // Основные геттеры
    public Function getFunction() {
        return function;
    }

    public double getLeftBorder() {
        return leftBorder;
    }

    public double getRightBorder() {
        return rightBorder;
    }

    public double getDiscretizationStep() {
        return discretizationStep;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    // Геттер и сеттер для синхронизации
    public boolean isDataProcessed() {
        return dataProcessed;
    }

    public void setDataProcessed(boolean processed) {
        this.dataProcessed = processed;
    }

    // Основные сеттеры
    public void setFunction(Function function) {
        this.function = function;
    }

    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    public void setDiscretizationStep(double discretizationStep) {
        this.discretizationStep = discretizationStep;
    }
}