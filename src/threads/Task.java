package threads;

import functions.*;

public class Task {
    private Function function; // Функция для интегрирования
    private double leftX;      // Левая граница интегрирования
    private double rightX;     // Правая граница интегрирования
    private double discretStep;       // Шаг дискретизации
    private int taskCount;     // Количество выполняемых заданий

    public Function getFunction() {
        return function;
    }
    public void setFunction(Function f) {
        this.function = f;
    }

    public double getLeftBorder() {
        return leftX;
    }
    public void setLeftBorder(double x) {
        this.leftX = x;
    }

    public double getRightBorder() {
        return rightX;
    }
    public void setRightBorder(double x) {
        this.rightX = x;
    }

    public double getDiscretStep() {
        return discretStep;
    }
    public void setDiscretStep(double step) {
        this.discretStep = step;
    }

    public int getTaskCount() {
        return taskCount;
    }
    public void setTaskCount(int n) {
        this.taskCount = n;
    }
}
