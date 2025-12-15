package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {

    private double x;
    private double y;

    // Конструктор с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Конструктор копирования
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    // Конструктор по умолчанию
    public FunctionPoint() {
        this(0, 0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }


    // Возвращает текстовое описание точки в формате (x; y)

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }


    // Сравнивает две точки на равенство координат с использованием метода compareDouble из ArrayTabulatedFunction

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        return ArrayTabulatedFunction.compareDouble(that.x, x) && ArrayTabulatedFunction.compareDouble(that.y, y);

    }


    //Вычисляет хэш-код точки на основе её координат

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        // Преобразуем double в два int значения и применяем XOR
        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }

    //Создает и возвращает копию текущей точки

    @Override
    public Object clone() {
        try {
            // Используем конструктор копирования для создания новой точки
            return new FunctionPoint(this);
        } catch (Exception e) {
            // В случае ошибки бросаем RuntimeException, так как клонирование не должно вызывать проверяемых исключений
            throw new RuntimeException("Ошибка при клонировании объекта FunctionPoint", e);
        }
    }
}
