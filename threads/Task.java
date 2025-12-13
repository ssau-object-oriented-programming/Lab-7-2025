package threads;

import functions.Function;

public class Task {
    public Function function;  // функция для интегрирования
    public double left;        // левая граница
    public double right;       // правая граница
    public double step;        // шаг интегрирования
    public int taskCount;      // сколько заданий нужно выполнить
    private boolean dataReady = false;
    private boolean completed = false;



    //конструктор
    public Task(Function function, double left, double right, double step, int taskCount) {
        this.function = function;
        this.left = left;
        this.right = right;
        this.step = step;
        this.taskCount = taskCount;

    }

    public Task() {
        this(null, 0, 0, 0, 0);
    }

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }
    
    
    public double getLeft() { return left; }
    public void setLeft(double left) { this.left = left; }
    
    public double getRight() { return right; }
    public void setRight(double right) { this.right = right; }
    
    public double getStep() { return step; }
    public void setStep(double step) { this.step = step; }
    
    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

     public void setAll(Function function, double left, double right, double step) {
        this.function = function;
        this.left = left;
        this.right = right;
        this.step = step;
    }

    public boolean isDataReady() { return dataReady; }
    public void setDataReady(boolean ready) { this.dataReady = ready; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    

}
