package functions;
import java.io.Serializable;

public class FunctionPoint implements Serializable{
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;


    public FunctionPoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    public FunctionPoint(FunctionPoint point){
        this.x=point.x;
        this.y=point.y;
    }

    FunctionPoint(){
        this(0.0, 0.0);
    }

    public double get_x(){
        return x;
    }

    public void set_x(double x) {
        this.x = x;
    }

    public double get_y(){
        return y;
    }

    public void set_y(double y) {
            this.y = y;
        }

    public String toString() {
        return String.format("(%.3f; %.3f)", x, y);
    }

    public boolean equals(Object o) {
        // Проверяем, является ли объект тем же самым
        if (this == o) return true;
        
        // Проверяем, является ли объект null или другого класса
        if (o == null || getClass() != o.getClass()) return false;
        
        // Приводим объект к FunctionPoint
        FunctionPoint that = (FunctionPoint) o;
        
        // Сравниваем координаты с учетом точности для double
        // Используем Double.compare для корректного сравнения double
        final double EPSILON = 1e-10;
        return Math.abs(that.x - x) < EPSILON && 
           Math.abs(that.y - y) < EPSILON;
    }

     public int hashCode() {
        // Получаем битовое представление double значений
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        // Разбиваем каждое 64-битное значение на два 32-битных
        int xHigh = (int)(xBits >>> 32);  // Старшие 4 байта
        int xLow = (int)(xBits & 0xFFFFFFFFL);  // Младшие 4 байта
        
        int yHigh = (int)(yBits >>> 32);  // Старшие 4 байта
        int yLow = (int)(yBits & 0xFFFFFFFFL);  // Младшие 4 байта
        
        // Применяем XOR ко всем четырем частям
        return xHigh ^ xLow ^ yHigh ^ yLow;
    }

    public FunctionPoint clone() {
        return new FunctionPoint(this); // Используем конструктор копирования
    }
}