package functions;

public interface TabulatedFunctionFactory {
    // создает табулированную ф-цию с нулевыми значениями
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount);

    // создает табулированную ф-цию с заданными значениями в точках
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values);

    // создает табулированную ф-цию из массива точек
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points);
}