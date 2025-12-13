package threads;

import functions.Function;
import functions.basic.Log;

public class Task {
    private Function function;      // ссылка на объект интегрируемой функции
    private double leftBorder;     // левая граница области интегрирования
    private double rightBorder;    // правая граница области интегрирования
    private double step;           // шаг дискретизации
    private int tasksCount;        // количество выполняемых заданий

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
}