package functions;

public interface TabulatedFunction extends Function, Cloneable, Iterable<FunctionPoint>{


    // Методы работы с точками
    int getPointsCount();
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    double getPointX(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    double getPointY(int index);
    void setPointY(int index, double y);
    void deletePoint(int index);
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;


    static boolean compareDouble(double a, double b) {
        final double epsilon = 1e-10;
        double diff = a - b;
        return Math.abs(diff) < epsilon;
    }

    Object clone();

}
