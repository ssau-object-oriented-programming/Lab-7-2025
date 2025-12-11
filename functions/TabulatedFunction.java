package functions;

import java.util.Iterator;

public interface TabulatedFunction extends Function, Cloneable, Iterable<FunctionPoint> {

    // Возвращает количество точек
    int getPointsCount();

    // Возвращает точку по индексу
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;

    // Заменяет точку по индексу
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // Возвращает X точки по индексу
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;

    // Устанавливает X точки по индексу
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // Возвращает Y точки по индексу
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;

    // Устанавливает Y точки по индексу
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // Удаляет точку по индексу
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;

    // Добавляет новую точку (сохраняя порядок)
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    Object clone() throws CloneNotSupportedException;

    Iterator<FunctionPoint> iterator();

}
