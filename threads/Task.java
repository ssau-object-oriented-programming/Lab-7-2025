package threads;

import functions.Function;

public class Task {
    private Function function;
    private double stepIntegration;
    private double leftX, rightX;
    private int iterations;
    private int currentIteration = 0;
    private boolean isExecuted = true;
    public Task() {};
    public Task(Function function, double stepIntegration, double leftX, double rightX, int iterations) {
        this.function = function;
        this.stepIntegration = stepIntegration;
        this.leftX = leftX;
        this.rightX = rightX;
        this.iterations = iterations;
    }
    public void setExecuted(boolean executed) { this.isExecuted = executed; }
    public boolean isExecuted() { return isExecuted; }
    public void incrementIteration() {
        this.currentIteration++;
        isExecuted = true;
    }
    public Function getFunction() { return function; }
    public double getStepIntegration() { return stepIntegration; }
    public double getLeftX() { return leftX; }
    public double getRightX() { return rightX; }
    public int getIterations() { return iterations; }
    public int getCurrentIteration() { return currentIteration; }
    public void setFunction(Function function) { this.function = function; }
    public void setStepIntegration(double stepIntegration) { this.stepIntegration = stepIntegration; }
    public void setLeftX(double leftX) { this.leftX = leftX; }
    public void setRightX(double rightX) { this.rightX = rightX; }
    public void setIterations(int iterations) { this.iterations = iterations; }
    public void setCurrentIteration(int currentIteration) { this.currentIteration = currentIteration; }
}
