package functions;

public interface TabulatedFunctionFactory {
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points, int count);
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointCount);
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values);
}
