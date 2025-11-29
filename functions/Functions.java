package functions;

import functions.meta.*;

public class Functions
{
    private Functions(){}

    public static Function shift(Function f, double shiftX, double shiftY)
    {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY)
    {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power)
    {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2)
    {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2)
    {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2)
    {
        return new Composition(f1, f2);
    }

    public static double definiteIntegralCalculation(Function func, double leftBorder, double rightBorder, double discretizationStep)
    {
        if(func == null)
            throw new IllegalArgumentException("Функция не может быть пустой");
        if(leftBorder < func.getLeftDomainBorder() || rightBorder > func.getRightDomainBorder())
            throw new IllegalArgumentException("Область интегрирования выходит за пределы области определения функции");
        if(leftBorder > rightBorder)
            throw new IllegalArgumentException("Левая граница интегрирования больше правой");
        if(discretizationStep <= 0)
            throw new IllegalArgumentException("Шаг дискретизации не может быть <= 0");

        double integralSum = 0;
        double currentX = leftBorder;

        while(currentX < rightBorder)
        {
            double nextX = Math.min(currentX + discretizationStep, rightBorder);
            double currentStep = nextX - currentX;

            integralSum += (func.getFunctionValue(currentX) + func.getFunctionValue(nextX)) * currentStep / 2;

            currentX = nextX;
        }

        return integralSum;
    }
}
