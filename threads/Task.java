package threads;

import functions.Function;

public class Task {
    private Function f;
    private double left;
    private double right;
    private double diskr;
    private int count;
    public Task(int cou) {
        count = cou;
    }

    public Function getF() {
        return f;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public int getCount() {
        return count;
    }

    public double getDiskr() {
        return diskr;
    }

    public void setF(Function f) {
        this.f = f;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public void setDiskr(double diskr) {
        this.diskr = diskr;
    }
}
