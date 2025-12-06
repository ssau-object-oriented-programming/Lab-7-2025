package functions;
import functions.meta.*;

public class Functions {
    // Приватный конструктор
    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    // НОВЫЙ МЕТОД (Лабораторная 6, Задание 1)
    // Вычисляет интеграл методом трапеций
    public static double integrate(Function function, double leftX, double rightX, double step) {
        // Проверка: границы интегрирования должны быть внутри области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Integration interval is out of function domain");
        }
        
        double area = 0.0;
        double currentX = leftX;
        
        while (currentX < rightX) {
            double nextX = currentX + step;
            // Если следующий шаг выходит за правую границу, обрезаем его
            if (nextX > rightX) {
                nextX = rightX;
            }
            
            double y1 = function.getFunctionValue(currentX);
            double y2 = function.getFunctionValue(nextX);
            
            // Формула площади трапеции: полусумма оснований, умноженная на высоту
            area += (y1 + y2) * (nextX - currentX) / 2.0;
            
            currentX = nextX;
        }
        
        return area;
    }
}
