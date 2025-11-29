package functions;

public interface TabulatedFunctionFactory {
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points);  // создает табулированную функцию из массива точек
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount);  // создает функцию с равномерными точками
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values);  // создает функцию с заданными значениями
}
