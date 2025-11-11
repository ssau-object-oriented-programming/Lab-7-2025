package functions;

import java.io.*;

public class FunctionPoint {
    private double x;
    private double y;
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point) {
        x = point.x;
        y = point.y;
    }
    public FunctionPoint() {
        x = 0;
        y = 0;
    }
    public double getx() { //Чтобы получить доступ к значению поля x из другого класса
        return x;
    }
    public double gety() { //Чтобы получить доступ к значению поля y из другого класса
        return y;
    }

    public String toString() {
        return "(" + x + "; " + y +')';
    }

    public boolean equals(Object o) {
        if (o instanceof FunctionPoint && Double.compare(((FunctionPoint) o).getx(), x) == 0 && Double.compare(((FunctionPoint) o).gety(), y) == 0) {//проверка типа класса
            return true;
        }
        return false;
    }

    public Object clone() {
        return new FunctionPoint(x, y);
    }

    public int hashCode() {
        long X = Double.doubleToLongBits(x); //преобразовали x и y в битовое представление
        long Y = Double.doubleToLongBits(y);
        return (int)(X >>> 32) ^ (int)(X & 0xFFFFFFFFL) ^ (int)(Y >>> 32) ^ (int)(Y & 0xFFFFFFFFL); //сдвигаем на 32 бита вправо получая старшие байты и применяем оператор побитового И, чтобы получить младшие 4 байта, применяем операцию XOR
    }
}