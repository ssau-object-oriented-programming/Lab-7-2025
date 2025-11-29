package functions;

import java.util.Objects;

public class FunctionPoint
{
    private static final double EPSILON = 1e-10;
    private double x;
    private double y;

    // конструктор по умолчанию
    public FunctionPoint()
    {
        this.x = 0;
        this.y = 0;
    }

    // конструктор с координатами
    public FunctionPoint(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // конструктор копирования
    public FunctionPoint(FunctionPoint point)
    {
        this.x = point.x;
        this.y = point.y;
    }

    // геттеры
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    // сеттеры
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "(" + x +";" + y + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FunctionPoint that = (FunctionPoint) obj;

        return Math.abs(that.x - x) < EPSILON &&
                Math.abs(that.y - y) < EPSILON;
    }

    @Override
    public int hashCode()
    {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        // Разбиваем каждый long на два int (старшие и младшие 32 бита)
        int x1 = (int)(xBits >> 32);
        int x2 = (int)xBits;
        int y1 = (int)(yBits >> 32);
        int y2 = (int)yBits;

        return x1 ^ x2 ^ y1 ^ y2;
    }

    @Override
    public Object clone()
    {
        return new FunctionPoint(x, y);
    }
}