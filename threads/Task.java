package threads;

import functions.Function;

public class Task {
    private Function function; // Интегрируемая функция
    private double left; // Левая граница интегрирования
    private double right; // Правая граница интегрирования
    private double step; // Шаг дискретизации
    private int tasksCount; // Количество выполняемых заданий
    
    public Task() {
    }
    public Function getFunction() {
        return function;
    }
    public void setFunction(Function function) {
        this.function = function;
    }
    public double getLeft() {
        return left;
    }
    public void setLeft(double left) {
        this.left = left;
    }
    public double getRight() {
        return right;
    }
    public void setRight(double right) {
        this.right = right;
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
}