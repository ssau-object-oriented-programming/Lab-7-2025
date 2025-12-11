package functions;

import functions.meta.*;

//Класс, содержащий вспомогательные статические методы для работы с функциями

public final class Functions {

    //Приватный конструктор запрещает создание объектов этого класса
    private Functions() {

    }

    //возвращает объект функции, полученной из исходной сдвигом вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    //возвращает объект функции, полученной из исходной масштабированием вдоль осей
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    //возвращает объект функции, являющейся заданной степенью исходной
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    //возвращает объект функции, являющейся суммой двух исходных
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    //возвращает объект функции, являющейся произведением двух исходных
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    //возвращает объект функции, являющейся композицией двух исходных
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    //возвращает значение определенного интеграла переданной функции
    public static double integrate(Function function, double leftX, double rightX, double step) {

        // Проверка: находится ли интервал интегрирования внутри области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Interval is out of function domain");
        }

        double area = 0; // Переменная для площади
        double currentX = leftX;

        // Цикл по шагам от левой границы до правой
        while (currentX < rightX) {
            double nextX = currentX + step;

            // Если следующий шаг вылезает за правую границу, обрезаем его
            if (nextX > rightX) {
                nextX = rightX;
            }

            // Вычисляем высоту трапеции (значения функции в точках)
            double y1 = function.getFunctionValue(currentX);
            double y2 = function.getFunctionValue(nextX);

            // Ширина текущего шага
            double currentStepSize = nextX - currentX;

            // Площадь трапеции:
            area += 0.5 * (y1 + y2) * currentStepSize;

            currentX = nextX;
        }

        return area;
    }
}