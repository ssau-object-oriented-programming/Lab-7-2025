package functions;
public interface TabulatedFunction extends Function, Iterable<FunctionPoint>{ //TabulatedFunction расширяет Function

    public int getPointsCount();

    public FunctionPoint getPoint(int index);

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;

    public double getPointX(int index);

    public void setPointX(int index, double x) throws InappropriateFunctionPointException;

    public double getPointY(int index);

    public void setPointY(int index, double y) throws InappropriateFunctionPointException;

    public void deletePoint(int index);

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    public Object clone();
}
