package functions;
import java.io.Serializable;

// Класс FunctionPoint описывает точку функции
public class FunctionPoint implements Serializable {

    private static final double EPS = 1e-9;
    private static final long serialVersionUID = 1L;

    // абсцисса
    private double x;

    // ордината
    private double y;

    // Конструктор, создающий точку с заданными координатами.
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Конструктор копирования, создает новую точку с такими же координатами, как у переданной точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    // Конструктор создаёт точку с координатами (0; 0)
    public FunctionPoint() {
        this(0.0, 0.0);
    }

    // Геттер - возвращает значение абсциссы данной точки
    public double getX() {
        return x;
    }

    // Сеттер - устанавливает новое значение абсциссы данной точки
    public void setX(double x) {
        this.x = x;
    }

    // Геттер - возвращает значение ординаты данной точки
    public double getY() {
        return y;
    }

    // Сеттер - устанавливает новое значение ординаты данной точки
    public void setY(double y) {
        this.y = y;
    }

    // возвращает текстовое значение точки
    public String toString() {

        // Форматирование до одной цифры после запятой для краткости
        return String.format("(%.1f; %.1f)", x, y);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        // Проверяем, что объект не null и имеет тот же класс
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x) &&
                Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y);
    }

    public int hashCode() {
        // Получаем 64-битное представление x и y
        long bitsX = Double.doubleToLongBits(x);
        long bitsY = Double.doubleToLongBits(y);

        // Объединяем 32-битные части x и y с помощью XOR, (bits >>> 32) берет старшие 32 бита, (int)bits берет младшие 32 бита
        int hashX = (int) (bitsX ^ (bitsX >>> 32));
        int hashY = (int) (bitsY ^ (bitsY >>> 32));

        // Объединяем хэши x и y
        return hashX ^ hashY;
    }

    public Object clone() {

        return new FunctionPoint(this.x, this.y);
    }

}
