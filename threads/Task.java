package threads;
import functions.Function;
import functions.Functions;

import java.io.FileOutputStream;

public class Task {
    private Function func;
    private double leftBorder;
    private double rightBorder;
    private double step;
    private int count;

    public Task(Function function, double leftBorder, double rightBorder, double step, int Count) {
        this.func = function;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.step = step;
        this.count = Count;
    }

    public void setFunc(Function func){
        this.func = func;
    }

    public Function getFunc(){
        return func;
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

    public int getCount() {
        return count;
    }

    public void setCount(int Count) {
        this.count = Count;
    }

}
