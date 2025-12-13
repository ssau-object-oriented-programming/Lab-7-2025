package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    // конструктор с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // конструктор копирования существующей точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    // конструктор по умолчанию
    public FunctionPoint() { x = 0; y = 0; }

    // возвращает координату x
    public double getX() { return x; }

    // возвращает координату y
    public double getY() { return y; }

    // устанавливает новое значение x
    public void setX(double x) { this.x = x; }

    // устанавливает новое значение y
    public void setY(double y) { this.y = y; }

    // возвращает текстовое описание точки
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    // сравнивает текущую точку с другим объектом
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        // сравниваем координаты с учетом погрешности
        final double EPSILON = 1e-10;
        return Math.abs(this.x - that.x) < EPSILON &&
                Math.abs(this.y - that.y) < EPSILON;
    }

    // вычисляет хэш-код точки на основе её координат
    @Override
    public int hashCode(){
        long xBit = Double.doubleToLongBits(this.x);
        long yBit = Double.doubleToLongBits(this.y);

        int x1 = (int)(xBit & 0xFFFFFFFF);
        int x2 = (int)(xBit >>> 32);
        int y1 = (int)(yBit & 0xFFFFFFFF);
        int y2 = (int)(yBit >>> 32);

        return x1 ^ x2 ^ y1 ^ y2;
    }

    // создает и возвращает копию текущей точки
    @Override
    public Object clone() {
        try {
            return new FunctionPoint(this);
        } catch (Exception e) {
            throw new InternalError("ошибка при клонировании: " + e.getMessage());
        }
    }
}