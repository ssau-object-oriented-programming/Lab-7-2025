package functions;

public interface TabulatedFunctionFactory
{
    /**
     * Создает табулированную функцию с границами по Х и заданным количеством точек
     * @param leftX левая граница по X
     * @param rightX правая граница по X
     * @param pointCount количество точек функции
     */
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointCount)
            throws IllegalArgumentException;

    /**
     * Создает табулированную функцию с границами по Х и заданными значениями точек по Y
     * @param leftX левая граница по X
     * @param rightX правая граница по X
     * @param values значения точек функции
     */
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException;

    /**
     * Создает табулированную функцию из имеющегося массива точек
     * @param points массив точек
     */
    public TabulatedFunction createTabulatedFunction(FunctionPoint[] points)
            throws IllegalArgumentException;
}
