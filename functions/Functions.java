package functions;
import functions.meta.*;

public class Functions {
    private Functions(){} // приватный конструктор чтобы нельзя было создать экземпляр

    public static double integral(Function func, double leftBorder, double rightBorder, double step) throws IllegalArgumentException{
        if(func.getLeftDomainBorder() > leftBorder) // проверяем что левая граница не меньше области определения функции
            throw new IllegalArgumentException("Inappropriate left border"); // если меньше бросаем исключение
        if(func.getRightDomainBorder() < rightBorder) // проверяем что правая граница не больше области определения функции
            throw new IllegalArgumentException("Inappropriate right border"); // если больше бросаем исключение
        if(leftBorder > rightBorder) // проверяем что левая граница меньше правой
            throw new IllegalArgumentException("Right border must be bigger than left border"); // если нет бросаем исключение

        double value = 0; // переменная для накопления результата интеграла
        double cur = leftBorder; // начинаем с левой границы
        while(cur < rightBorder){ // пока не дойдем до правой границы
            double next = Math.min(cur + step, rightBorder); // вычисляем следующую точку или доходим до правой границы
            double segment = next-cur; // вычисляем длину текущего отрезка

            double curVal = func.getFunctionValue(cur); // вычисляем значение функции в текущей точке
            double nextVal = func.getFunctionValue(next); // вычисляем значение функции в следующей точке

            double segmentArea = (curVal + nextVal) * segment / 2; // вычисляем площадь трапеции на отрезке

            value += segmentArea; // добавляем площадь к общему результату
            cur = next; // переходим к следующему отрезку
        }
        return value; // возвращаем вычисленное значение интеграла
    }

    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f,shiftX, shiftY); // создаем функцию сдвига
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f,scaleX,scaleY); // создаем функцию масштабирования
    }
    public static Function power(Function f, double power){
        return new Power(f, power); // создаем функцию возведения в степень
    }

    public static Function sum(Function f1, Function f2){
        return new Sum(f1, f2); // создаем функцию суммы
    }

    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2); // создаем функцию произведения
    }

    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2); // создаем композицию функций
    }
}