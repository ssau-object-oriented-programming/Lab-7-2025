package functions;

// Наследуемся от Iterable<FunctionPoint>, чтобы можно было использовать в цикле for-each
public interface TabulatedFunction extends Function, Cloneable, java.io.Serializable, Iterable<FunctionPoint> {
    int getPointsCount();
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    double getPointX(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    double getPointY(int index);
    void setPointY(int index, double y);
    void deletePoint(int index);
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    
    Object clone() throws CloneNotSupportedException;
}
