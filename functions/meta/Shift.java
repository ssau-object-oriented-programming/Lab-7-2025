package functions.meta;

import functions.Function;

public class Shift implements Function{
    private Function f;
    private double shiftX;
    private double shiftY;

    public Shift(Function f, double shiftX, double shiftY) {
        this.f = f;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {
        // Для корректной работы с бесконечностями
        double left = f.getLeftDomainBorder();
        if (Double.isInfinite(left)) {
            return left; // ±∞ + shiftX = ±∞
        }
        return left + shiftX;
    }

    public double getRightDomainBorder() {
        double right = f.getRightDomainBorder();
        if (Double.isInfinite(right)) {
            return right;
        }
        return right + shiftX;
    }
     public double getFunctionValue(double x) {
        // Обратное преобразование x
        double originalX = x - shiftX;
        
        // Получаем значение исходной функции
        double originalY = f.getFunctionValue(originalX);
        
        if (Double.isNaN(originalY)) {
            return Double.NaN;
        }
        
        // Сдвиг по оси Y
        return originalY + shiftY;
    }

}
