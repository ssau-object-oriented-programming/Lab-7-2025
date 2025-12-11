package functions;

public interface TabulatedFunctionFactory {

    TabulatedFunction create(double leftX, double rightX, int pointsCount);

    TabulatedFunction create(double leftX, double rightX, double[] values);

    TabulatedFunction create(FunctionPoint[] points);
}