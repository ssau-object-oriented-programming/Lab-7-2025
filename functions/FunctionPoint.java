package functions;

import java.io.*;
import java.util.Objects;

/**
 * Класс, объекты которого описывают одну точку
 * табулированной функции
 */
public class FunctionPoint implements Serializable {
    // координаты точки
    private double x;
    private double y;
    /**
     * Конструктор без параметров<br>
     * Создает точку с координатами (0,0)
     */
    public FunctionPoint() {
        this.x = 0.0;
        this.y = 0.0;
    }
    /**
     * Конструктор с заданными координатами точки
     * @param x значение по оси X
     * @param y значение по оси Y
     */
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Конструктор копирования<br>
     * <i>Примечание: все же лучше использовать переопределение метода clone(), но ТЗ такое</i>
     * @param other другой объект, с которого надо скопировать координаты точки
     */
    public FunctionPoint(FunctionPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    /** Метод получения значения точки по оси X
     * @return значение по оси X
     */
    public double getX() { return this.x; }
    /** Метод получения значения точки по оси Y
     * @return значение по оси Y
     */
    public double getY() { return this.y; }

    /** Метод установки значения точки по оси X
     * @param x значение точки по оси X
     */
    public void setX(double x) { this.x = x; }
    /** Метод установки значения точки по оси Y
     * @param y значение точки по оси Y
     */
    public void setY(double y) { this.y = y; }
    @Override
    public String toString() {
        return "(" + this.x + ';' + this.y + ')';
    }

    @Override
    public boolean equals(Object obj) { // синтаксический сахар
        if (!(obj instanceof FunctionPoint other)) return false;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y); // думаю, готовая реализация
    } // все же лучше, чем использовать свои методы
    // А если x = x0, y = x0, то меняй их местами, хэш будет совпадать

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new FunctionPoint(this);
    } // давайте подумаем. Если так подумать, нужно вернуть объект-копию
    // два абсолютно одинаковых объекта все равно же разные
    // Их различает ссылки + они имеют исключительно СВОИ поля
}