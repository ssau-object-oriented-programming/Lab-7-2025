package functions;

import java.io.Externalizable;

public interface TabulatedFunction extends Function, Externalizable, Cloneable, Iterable<FunctionPoint> {
    // клонировать объект
    public Object clone();

    // получить общее количество точек в функции
    public int getPointsCount();

    // получить копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;

    // заменить точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // получить координату x точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;

    // установить новую координату x для точки по индексу
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // получить координату y точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;

    // установить новую координату y для точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // удалить точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;

    // добавить новую точку в функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}