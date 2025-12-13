package functions.meta;

import functions.Function;

public class Scale implements Function{
     private Function f;       // Исходная функция
    private double scaleX;    // Коэффициент масштабирования по оси X
    private double scaleY;    // Коэффициент масштабирования по оси Y
    
    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        if (scaleX == 0) return Double.NaN;
        return f.getLeftDomainBorder() * scaleX;
    }

    public double getRightDomainBorder() {
        if (scaleX == 0) return Double.NaN;
        return f.getRightDomainBorder() * scaleX;
        }

     public double getFunctionValue(double x) {
        // Проверяем, что точка принадлежит масштабированной области определения
        if (x < Math.min(getLeftDomainBorder(), getRightDomainBorder()) || 
            x > Math.max(getLeftDomainBorder(), getRightDomainBorder())) {
            return Double.NaN;
        }

        double originalX = x / scaleX;

        double originalY = f.getFunctionValue(originalX);
        if (Double.isNaN(originalY)) {
            return Double.NaN;
        }

        return originalY * scaleY;
        }
}
