package functions;

import java.io.Serializable;

import static functions.DoubleComparison.doubleEq;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    //Конструкторы
    //Создает объект точки с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Создает объект точки с теми же координатами, что и у указанной точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    //Создает точкку (0,0)
    public FunctionPoint() {
        this(0,0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionPoint)) {
            return false;
        }
        FunctionPoint otherPoint = (FunctionPoint) o;

        return doubleEq(this.x, otherPoint.x) && doubleEq(this.y, otherPoint.y);
    }

    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }

    public Object clone() {
        return new FunctionPoint(this);
    }
}
